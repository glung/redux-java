package com.redux;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

public class TodoReducer implements Reducer<ActionCreator.Action, TodoState> {

    @Inject
    public TodoReducer() {
        // For dagger.
    }

    @Override public TodoState call(ActionCreator.Action action, TodoState state) {
        switch (action.getType()) {
            case ActionCreator.Action.ADD:
                return state.add(new Todo(state.nextFreeId(), action.getText(), false));

            case ActionCreator.Action.DELETE:
                return state.remove(state.find(action.getId()));

            case ActionCreator.Action.COMPLETE:
                final Todo todoCompleted = state.find(action.getId());
                return state
                        .remove(todoCompleted)
                        .add(new Todo(todoCompleted.id, todoCompleted.text, action.isCompleted()));

            case ActionCreator.Action.COMPLETE_ALL:
                final Set<Todo> todoListToComplete = state.getTodoList();
                final List<Todo> completedList = new ArrayList<>(todoListToComplete.size());

                for (Todo todo : todoListToComplete) {
                    completedList.add(new Todo(todo.id, todo.text, action.isCompleted()));
                }
                return TodoState.from(completedList);

            case ActionCreator.Action.CLEAR_ALL_COMPLETED:
                final Set<Todo> todoListToClean = state.getTodoList();
                final List<Todo> clearedList = new ArrayList<>(todoListToClean.size());

                for (Todo todo : todoListToClean) {
                    if (!todo.isCompleted) {
                        clearedList.add(new Todo(todo.id, todo.text, false));
                    }
                }
                return TodoState.from(clearedList);
            default:
                return state;
        }
    }

}
