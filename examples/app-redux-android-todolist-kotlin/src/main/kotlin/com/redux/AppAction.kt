package com.redux

sealed class AppAction : Action {
    object Init : AppAction()

    data class Add(val text: String, val isCompleted: Boolean) : AppAction()
    data class Delete(val id: Int) : AppAction()
    data class Complete(val id: Int, val isCompleted: Boolean) : AppAction()
    data class CompleteAll(val isCompleted: Boolean) : AppAction()

    object ClearCompleted : AppAction()

    data class Fetching(val isFetching: Boolean) : AppAction()
}

