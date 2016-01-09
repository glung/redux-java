package com.redux

import com.github.salomonbrys.kotson.*
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.redux.devtools.Converter

object ActionConverter : Converter<TodoListAction> {
    override fun toJson(element: TodoListAction): String = when (element) {
        is TodoListAction.Init -> jsonObject(
                "type" to "Init"
        ).toString()
        is TodoListAction.Add -> jsonObject(
                "type" to "Add",
                "isCompleted" to element.isCompleted.toJson(),
                "text" to element.text.toJson()
        ).toString()
        is TodoListAction.Delete -> jsonObject(
                "type" to "Delete",
                "id" to element.id.toJson()
        ).toString()
        is TodoListAction.Complete -> jsonObject(
                "type" to "Complete",
                "id" to element.id.toJson(),
                "isCompleted" to element.isCompleted.toJson()
        ).toString()
        is TodoListAction.CompleteAll -> jsonObject(
                "type" to "CompleteAll",
                "isCompleted" to element.isCompleted.toJson()
        ).toString()
        is TodoListAction.ClearCompleted -> jsonObject(
                "type" to "ClearCompleted"
        ).toString()
        is TodoListAction.Fetching -> jsonObject(
                "type" to "Fetching",
                "isFetching" to element.isFetching.toJson()
        ).toString()
    }

    override fun fromJson(json: String): TodoListAction {
        val element = JsonParser().parse(json)
        when(element.get("type").string) {
            "Init" -> return TodoListAction.Init
            "Add" -> return TodoListAction.Add(element.get("text").string, element.get("isCompleted").bool)
            "Delete" -> return TodoListAction.Delete(element.get("id").int)
            "Complete" -> return TodoListAction.Complete(element.get("id").int, element.get("isCompleted").bool)
            "CompleteAll" -> return TodoListAction.CompleteAll(element.get("isCompleted").bool)
            "ClearCompleted" -> return TodoListAction.ClearCompleted
            "Fetching" -> return TodoListAction.Fetching(element.get("isFetching").bool)
            else -> throw IllegalArgumentException("Unknown type")
        }
    }
}