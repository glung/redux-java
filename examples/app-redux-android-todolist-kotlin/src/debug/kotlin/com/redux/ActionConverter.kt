package com.redux

import com.github.salomonbrys.kotson.*
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser

class ActionConverter() : Converter<AppAction> {
    override fun toJson(element: AppAction): String = when (element) {
        is AppAction.Init -> jsonObject(
                "type" to "Init"
        ).toString()
        is AppAction.Add -> jsonObject(
                "type" to "Add",
                "isCompleted" to element.isCompleted.toJson(),
                "text" to element.text.toJson()
        ).toString()
        is AppAction.Delete -> jsonObject(
                "type" to "Delete",
                "id" to element.id.toJson()
        ).toString()
        is AppAction.Complete -> jsonObject(
                "type" to "Complete",
                "id" to element.id.toJson(),
                "isCompleted" to element.isCompleted.toJson()
        ).toString()
        is AppAction.CompleteAll -> jsonObject(
                "type" to "CompleteAll",
                "isCompleted" to element.isCompleted.toJson()
        ).toString()
        is AppAction.ClearCompleted -> jsonObject(
                "type" to "ClearCompleted"
        ).toString()
        is AppAction.Fetching -> jsonObject(
                "type" to "Fetching",
                "isFetching" to element.isFetching.toJson()
        ).toString()
    }

    override fun fromJson(json: String): AppAction {
        val element = JsonParser().parse(json)
        when(element.get("type").string) {
            "Init" -> return AppAction.Init
            "Add" -> return AppAction.Add(element.get("text").string, element.get("isCompleted").bool)
            "Delete" -> return AppAction.Delete(element.get("id").int)
            "Complete" -> return AppAction.Complete(element.get("id").int, element.get("isCompleted").bool)
            "CompleteAll" -> return AppAction.CompleteAll(element.get("isCompleted").bool)
            "ClearCompleted" -> return AppAction.ClearCompleted
            "Fetching" -> return AppAction.Fetching(element.get("isFetching").bool)
            else -> throw IllegalArgumentException("Unknown type")
        }
    }
}