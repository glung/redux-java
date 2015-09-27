package com.redux

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonIOException

class StateConverter(val gson: Gson) : Converter<AppState> {

    override fun fromJson(json: String): AppState {
        val list = gson.fromJson<List<Todo>>(json)
        return if (list != null) AppState(list, false) else throw JsonIOException("Could not parse $json")
    }

    override fun toJson(state: AppState): String {
        return gson.toJson(state.list)
    }
}
