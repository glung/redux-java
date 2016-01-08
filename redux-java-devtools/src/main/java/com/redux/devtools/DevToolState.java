package com.redux.devtools;

import java.util.Collections;
import java.util.List;

public class DevToolStateZ<A, S> {

    final long timestamp;
    final List<A> stagedActions;
    final List<Integer> skippedActionIndexes;
    final List<S> computedStates;
    final List<Long> startedAt;
    final List<Long> finishedAt;
    final int currentStateIndex;
    final S committedState;

    public DevToolState(long timestamp,
                        S committedState,
                        List<A> stagedActions,
                        List<Integer> skippedActionIndexes,
                        List<S> computedStates,
                        List<Long> startedAt,
                        List<Long> finishedAt,
                        int currentStateIndex) {
        this.timestamp = timestamp;
        this.committedState = committedState;
        this.stagedActions = Collections.unmodifiableList(stagedActions);
        this.skippedActionIndexes = Collections.unmodifiableList(skippedActionIndexes);
        this.computedStates = Collections.unmodifiableList(computedStates);
        this.startedAt = Collections.unmodifiableList(startedAt);
        this.finishedAt = Collections.unmodifiableList(finishedAt);
        this.currentStateIndex = currentStateIndex;
    }

}
