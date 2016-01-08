package com.redux

sealed class AppAction {

    object Init : AppAction()
    class Add(val text: String, val isCompleted: Boolean) : AppAction()
    class Delete(val id: Int) : AppAction()
    class Complete(val id: Int, val isCompleted: Boolean) : AppAction()
    class CompleteAll(val isCompleted: Boolean) : AppAction()
    object ClearCompleted : AppAction()
    class Fetching(val isFetching: Boolean) : AppAction()
}

