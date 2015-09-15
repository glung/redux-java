package com.redux;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

class StateConverter implements Converter<TodoState> {
    private final Gson gson;

    StateConverter(Gson gson) {
        this.gson = gson;
    }

    @Override public TodoState fromJson(String json) {
        final List<Todo> todos = gson.fromJson(json, new TypeToken<ArrayList<Todo>>() {}.getType());
        return TodoState.from(todos);
    }

    @Override public String toJson(TodoState state) {
        return gson.toJson(state.getTodoList());
    }
}
