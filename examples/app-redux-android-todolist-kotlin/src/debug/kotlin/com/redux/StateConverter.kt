package com.redux

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonIOException
import com.redux.devtools.Converter

class StateConverter(val gson: Gson) : Converter<TodoListState> {

    override fun fromJson(json: String): TodoListState {
        val list = gson.fromJson<List<Todo>>(json)
        return if (list != null) TodoListState(list, false) else throw JsonIOException("Could not parse $json")
    }

    override fun toJson(state: TodoListState): String {
        return gson.toJson(state.list)
    }
}
