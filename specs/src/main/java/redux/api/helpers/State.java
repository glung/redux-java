package redux.api.helpers;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

public final class State {
    final List<Todo> todos;

    public State(Todo... todos) {
        this(asList(todos));
    }

    public State(List<Todo> todos) {
        this.todos = unmodifiableList(todos);
    }

    public State() {
        this(Collections.<Todo>emptyList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final State state = (State) o;
        return todos != null ? todos.equals(state.todos) : state.todos == null;

    }

    @Override
    public int hashCode() {
        return todos != null ? todos.hashCode() : 0;
    }
}
