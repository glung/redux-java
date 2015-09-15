package com.redux;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

public class TodoReducer implements Reducer<ActionCreator.Action, AppState> {

    @Inject
    public TodoReducer() {
        // For dagger.
    }

    @Override public AppState call(ActionCreator.Action action, AppState state) {
        return new AppState(
                reduceList(action, state.todoList),
                reduceUiState(action, state.isFetching)
        );
    }

    private TodoList reduceList(ActionCreator.Action action, TodoList state) {
        final Bundle bundle = action.bundle;
        switch (action.type) {
            case ActionCreator.Action.KIND_ADD:
                return state.add(new Todo(state.nextFreeId(), bundle.getString(ActionCreator.Action.TEXT), bundle.getBoolean(ActionCreator.Action.IS_COMPLETED, false)));

            case ActionCreator.Action.KIND_DELETE:
                return state.remove(state.find(bundle.getInt(ActionCreator.Action.ID)));

            case ActionCreator.Action.KIND_COMPLETE:
                final Todo todoCompleted = state.find(bundle.getInt(ActionCreator.Action.ID));
                return state
                        .remove(todoCompleted)
                        .add(new Todo(todoCompleted.id, todoCompleted.text, bundle.getBoolean(ActionCreator.Action.IS_COMPLETED)));

            case ActionCreator.Action.KIND_COMPLETE_ALL:
                final Set<Todo> todoListToComplete = state.get();
                final List<Todo> completedList = new ArrayList<>(todoListToComplete.size());

                for (Todo todo : todoListToComplete) {
                    completedList.add(new Todo(todo.id, todo.text, bundle.getBoolean(ActionCreator.Action.IS_COMPLETED)));
                }
                return TodoList.from(completedList);

            case ActionCreator.Action.KIND_CLEAR_ALL_COMPLETED:
                final Set<Todo> todoListToClean = state.get();
                final List<Todo> clearedList = new ArrayList<>(todoListToClean.size());

                for (Todo todo : todoListToClean) {
                    if (!todo.isCompleted) {
                        clearedList.add(new Todo(todo.id, todo.text, false));
                    }
                }
                return TodoList.from(clearedList);
            default:
                return state;
        }
    }

    private boolean reduceUiState(ActionCreator.Action action, boolean isFetching) {
        switch (action.type) {
            case ActionCreator.Action.KIND_FETCHING:
                return action.bundle.getBoolean(ActionCreator.Action.IS_FETCHING);
            default:
                return isFetching;
        }
    }
}
