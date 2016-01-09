package com.redux

val reducer = { action: TodoListAction, state: TodoListState ->

    fun nextFreeIndex(list: List<Todo>): Int {
        return if (list.isEmpty()) 0 else list.last().id + 1;
    }

    when (action) {
        is TodoListAction.Init -> state
        is TodoListAction.Add -> state.copy(state.list + Todo(nextFreeIndex(state.list), action.text, action.isCompleted))
        is TodoListAction.Delete -> state.copy(state.list.filter { it.id != action.id })
        is TodoListAction.Complete -> state.copy(state.list.map { if (it.id == action.id) it.copy(isCompleted = action.isCompleted) else it })
        is TodoListAction.CompleteAll -> state.copy(state.list.map { it.copy(isCompleted = action.isCompleted) })
        is TodoListAction.ClearCompleted -> state.copy(state.list.filter { it.isCompleted.not() })
        is TodoListAction.Fetching -> state.copy(isFetching = action.isFetching)
    }
}
