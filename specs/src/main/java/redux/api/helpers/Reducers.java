package redux.api.helpers;

import redux.api.Reducer;

import java.util.ArrayList;

public class Reducers {
    private static int nexId(State state) {
        int maxId = 0;
        for (Todo todo : state.todos) {
            maxId = Math.max(todo.id, maxId);
        }
        return maxId + 1;
    }

    public static final Reducer<State> TODOS = new Reducer<State>() {
        @Override
        public State reduce(State state, Object action) {
            if (action instanceof Actions.AddTodo) {
                final String message = ((Actions.AddTodo) action).message;
                final ArrayList<Todo> newTodos = new ArrayList<>();
                newTodos.addAll(state.todos);
                newTodos.add(new Todo(nexId(state), message));
                return new State(newTodos);
            } else {
                return state;
            }
        }
    };

    public static final Reducer<State> TODOS_REVERSE = new Reducer<State>() {
        @Override
        public State reduce(State state, Object action) {
            if (action instanceof Actions.AddTodo) {
                final String message = ((Actions.AddTodo) action).message;
                final ArrayList<Todo> newTodos = new ArrayList<>();
                newTodos.add(new Todo(nexId(state), message));
                newTodos.addAll(state.todos);
                return new State(newTodos);
            } else {
                return state;
            }
        }
    };
}
