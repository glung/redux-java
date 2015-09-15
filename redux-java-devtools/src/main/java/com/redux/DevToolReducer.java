package com.redux;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class DevToolReducer<A extends Action, S extends State> implements Reducer<DevToolAction<A>, DevToolState<A, S>> {
    private final A initAction;
    private final S initialState;
    private final Reducer<A, S> reducer;

    public DevToolReducer(A initAction, S initialState, Reducer<A, S> reducer) {
        this.initAction = initAction;
        this.initialState = initialState;
        this.reducer = reducer;
    }

    @Override public DevToolState<A, S> call(DevToolAction<A> devToolAction, DevToolState<A, S> devToolState) {
        S committedState = devToolState.committedState;
        int currentStateIndex = devToolState.currentStateIndex;
        List<A> stagedActions = new ArrayList<>(devToolState.stagedActions);
        List<Integer> skippedActionIndexes = new ArrayList<>(devToolState.skippedActionIndexes);

        switch (devToolAction.devToolActionType) {
            case DevToolAction.PERFORM_ACTION:
                if (currentStateIndex == stagedActions.size() - 1) {
                    currentStateIndex++;
                }
                stagedActions.add(devToolAction.getApplicationAction());
                break;
            case DevToolAction.RESET:
                committedState = initialState;
                stagedActions = Collections.singletonList(initAction);
                skippedActionIndexes.clear();
                currentStateIndex = 0;
                break;
            case DevToolAction.COMMIT:
                committedState = devToolState.computedStates.get(currentStateIndex);
                stagedActions = Collections.singletonList(initAction);
                skippedActionIndexes.clear();
                currentStateIndex = 0;
                break;
            case DevToolAction.ROLLBACK:
                stagedActions = Collections.singletonList(initAction);
                skippedActionIndexes.clear();
                currentStateIndex = 0;
                break;
            case DevToolAction.JUMP_TO_STATE:
                currentStateIndex = devToolAction.getIndex();
                break;
            case DevToolAction.ENABLE:
                if (devToolAction.isEnabled()) {
                    skippedActionIndexes.remove((Integer) devToolAction.getIndex());
                } else {
                    skippedActionIndexes.add(devToolAction.getIndex());
                }
                break;
            default:
                break;
        }

        return reduceStates(stagedActions, skippedActionIndexes, currentStateIndex, committedState);
    }

    private DevToolState<A, S> reduceStates(List<A> stagedActions, List<Integer> skippedActionIndexes, int currentStateIndex, S committedState) {
        final List<Long> startedAt = new ArrayList<>(stagedActions.size());
        final List<Long> finishedAt = new ArrayList<>(stagedActions.size());
        final List<S> computedStates = new ArrayList<>();

        S previousState = committedState;
        for (int index = 0; index < stagedActions.size(); index++) {
            final A newAction = stagedActions.get(index);

            startedAt.add(System.currentTimeMillis());
            final S newState = reduce(skippedActionIndexes, previousState, index, newAction);
            finishedAt.add(System.currentTimeMillis());

            computedStates.add(newState);
            previousState = newState;
        }

        return new DevToolState<>(
                System.currentTimeMillis(),
                committedState,
                stagedActions,
                skippedActionIndexes,
                computedStates,
                startedAt,
                finishedAt,
                currentStateIndex
        );
    }

    private S reduce(List<Integer> skippedActionIndexes, S previousState, int actionIndex, A newAction) {
        if (skippedActionIndexes.contains(actionIndex)) {
            return previousState;
        } else {
            return reducer.call(newAction, previousState);
        }
    }

}
