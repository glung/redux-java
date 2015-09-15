package com.redux;


import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class TodoState implements State {
    private static final Comparator<Todo> TODO_COMPARATOR = new Comparator<Todo>() {
        @Override public int compare(Todo lhs, Todo rhs) {
            return lhs.id - rhs.id;
        }
    };

    private final TreeSet<Todo> todoList;

    public static TodoState create() {
        return new TodoState();
    }

    public static TodoState from(Collection<Todo> todos) {
        return new TodoState(todos);
    }

    public static TodoState from(Todo todo) {
        return new TodoState(Collections.singleton(todo));
    }

    public TodoState() {
        this.todoList = new TreeSet<>(TODO_COMPARATOR);
    }

    private TodoState(Collection<Todo> todos) {
        this.todoList = new TreeSet<>(TODO_COMPARATOR);
        this.todoList.addAll(todos);
    }

    public TodoState add(Todo todo) {
        final TodoState copy = new TodoState(todoList);
        copy.todoList.add(todo);
        return copy;
    }

    public TodoState remove(Todo todo) {
        final TodoState copy = new TodoState(todoList);
        copy.todoList.remove(todo);
        return copy;
    }

    public Todo find(int id) {
        for (Todo todo : todoList) {
            if (todo.id == id) {
                return todo;
            }
        }
        throw new IllegalArgumentException("Could not find TODO with id:     " + id);
    }

    public int nextFreeId() {
        if (todoList.isEmpty()) {
            return 0;
        }
        // Note : good example on debugging with the dev tool
        // the usage of first instead of last.
        return todoList.last().id + 1;
    }

    public Set<Todo> getTodoList() {
        return Collections.unmodifiableSet(todoList);
    }

}
