package com.redux;

import com.google.gson.Gson;

class ActionConverter implements Converter<ActionCreator.Action> {

    private final Gson gson;

    ActionConverter(Gson gson) {
        this.gson = gson;
    }

    @Override public ActionCreator.Action fromJson(String json) {
        return gson.fromJson(json, ActionCreator.Action.class);
    }

    @Override public String toJson(ActionCreator.Action action) {
        return gson.toJson(action);
    }
}
