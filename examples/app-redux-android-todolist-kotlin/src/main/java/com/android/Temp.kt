package com.android

import com.redux.*
import com.redux.devtools.DevToolAction
import com.redux.devtools.DevToolState
import com.redux.devtools.DevTools

fun provideStore(): Store<TodoListAction, TodoListState> {
    val devTools = DevTools<TodoListAction, TodoListState>()

    fun makeCreateStore() = {
        state: DevToolState<TodoListAction, TodoListState>,
        reducer: (DevToolAction, DevToolState<TodoListAction, TodoListState>) -> DevToolState<TodoListAction, TodoListState>
        ->
        createStore(state, reducer)
    }

    return devTools.instrument(makeCreateStore())(reducer, TodoListState())
}
