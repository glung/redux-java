package com.redux;

import com.google.common.base.MoreObjects;

public class AppState implements State {
    public final TodoList todoList;
    public final boolean isFetching;

    public AppState(TodoList todoList, boolean isFetching) {
        this.todoList = todoList;
        this.isFetching = isFetching;
    }

    @Override public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("isFetching", isFetching)
                .add("todoState", todoList)
                .toString();
    }

    public static AppState create() {
        return new AppState(TodoList.create(), false);
    }
    public static AppState create(TodoList todoList) {
        return new AppState(todoList, false);
    }
}
