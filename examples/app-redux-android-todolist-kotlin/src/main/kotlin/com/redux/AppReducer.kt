package com.redux

val reducer: Reducer<AppAction, AppState> = Reducer({ action: AppAction, state: AppState ->

    fun nextFreeIndex(list: List<Todo>): Int {
        return if (list.isEmpty()) 0 else list.last().id + 1;
    }

    when (action) {
        is AppAction.Init -> state
        is AppAction.Add -> state.copy(state.list + Todo(nextFreeIndex(state.list), action.text, action.isCompleted))
        is AppAction.Delete -> state.copy(state.list.filter { it.id != action.id })
        is AppAction.Complete -> state.copy(state.list.map { if (it.id == action.id) it.copy(isCompleted = action.isCompleted) else it })
        is AppAction.CompleteAll -> state.copy(state.list.map { it.copy(isCompleted = action.isCompleted) })
        is AppAction.ClearCompleted -> state.copy(state.list.filter { it.isCompleted.not() })
        is AppAction.Fetching -> state.copy(isFetching = action.isFetching)
    }
})


