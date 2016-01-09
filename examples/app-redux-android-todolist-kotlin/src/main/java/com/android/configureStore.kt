package com.android

import com.redux.TodoListAction
import com.redux.TodoListState
import com.redux.createStore
import com.redux.devtools.DevTools
import com.redux.todoListReducer

val devTools = DevTools<TodoListAction, TodoListState>()
val createStoreForDev = devTools.instrument()({ state, reducer -> createStore(state, reducer) })

fun configureStore(initialState: TodoListState) = createStoreForDev(initialState, todoListReducer)
