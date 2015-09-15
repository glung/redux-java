package com.redux;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

class StateConverter implements Converter<AppState> {
    private final Gson gson;

    StateConverter(Gson gson) {
        this.gson = gson;
    }

    @Override public AppState fromJson(String json) {
        final List<Todo> todos = gson.fromJson(json, new TypeToken<ArrayList<Todo>>() {}.getType());
        return AppState.create(TodoList.from(todos));
    }

    @Override public String toJson(AppState state) {
        return gson.toJson(state.todoList);
    }
}
