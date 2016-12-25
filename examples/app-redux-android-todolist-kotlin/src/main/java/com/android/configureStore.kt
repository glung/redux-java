package com.android

import com.android.todolist.BuildConfig
import com.redux.*
import com.redux.devtools.DevTools

val devTools = DevTools<TodoListAction, TodoListState>()
val instrument = devTools.instrument()({ initAction, initState, reducer -> createStore(initAction, initState, reducer) })

fun configureProdStore(initialState: TodoListState): Store<TodoListAction, TodoListState>
        = createStore(TodoListAction.Init, initialState, todoListReducer)

fun configureDevStore(initialState: TodoListState)
        = instrument(TodoListAction.Init, initialState, todoListReducer)

fun configureStore(initialState: TodoListState): Store<TodoListAction, TodoListState> =
        if (BuildConfig.DEBUG) configureDevStore(initialState)
        else configureProdStore(initialState)
