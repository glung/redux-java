package com.redux;

import com.google.common.base.Objects;

public class Todo {
    public final int id;
    public final String text;
    public final boolean isCompleted;

    Todo(int id, String text, boolean isCompleted) {
        this.id = id;
        this.text = text;
        this.isCompleted = isCompleted;
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Todo todo = (Todo) o;
        return Objects.equal(id, todo.id) &&
                Objects.equal(isCompleted, todo.isCompleted) &&
                Objects.equal(text, todo.text);
    }

    @Override public int hashCode() {
        return Objects.hashCode(id, text, isCompleted);
    }
}
