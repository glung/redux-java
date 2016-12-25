package com.redux

data class TodoListState(val list: List<Todo> = listOf<Todo>(), val isFetching: Boolean = false)

data class Todo(val id: Int, val text: String, val isCompleted: Boolean)
