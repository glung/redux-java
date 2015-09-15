package com.redux;


import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class TodoList {
    private static final Comparator<Todo> TODO_COMPARATOR = new Comparator<Todo>() {
        @Override public int compare(Todo lhs, Todo rhs) {
            return lhs.id - rhs.id;
        }
    };

    private final TreeSet<Todo> todos;

    public static TodoList create() {
        return new TodoList();
    }

    public static TodoList from(Collection<Todo> todos) {
        return new TodoList(todos);
    }

    public static TodoList from(Todo todo) {
        return new TodoList(Collections.singleton(todo));
    }

    public TodoList() {
        this.todos = new TreeSet<>(TODO_COMPARATOR);
    }

    private TodoList(Collection<Todo> todos) {
        this.todos = new TreeSet<>(TODO_COMPARATOR);
        this.todos.addAll(todos);
    }

    public TodoList add(Todo todo) {
        final TodoList copy = new TodoList(todos);
        copy.todos.add(todo);
        return copy;
    }

    public TodoList remove(Todo todo) {
        final TodoList copy = new TodoList(todos);
        copy.todos.remove(todo);
        return copy;
    }

    public Todo find(int id) {
        for (Todo todo : todos) {
            if (todo.id == id) {
                return todo;
            }
        }
        throw new IllegalArgumentException("Could not find TODO with id:     " + id);
    }

    public int nextFreeId() {
        if (todos.isEmpty()) {
            return 0;
        }
        // Note : good example on debugging with the dev tool
        // the usage of first instead of last.
        return todos.last().id + 1;
    }

    public Set<Todo> get() {
        return Collections.unmodifiableSet(todos);
    }
}
