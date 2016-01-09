package com.redux

sealed class TodoListAction {

    object Init : TodoListAction()
    class Add(val text: String, val isCompleted: Boolean) : TodoListAction()
    class Delete(val id: Int) : TodoListAction()
    class Complete(val id: Int, val isCompleted: Boolean) : TodoListAction()
    class CompleteAll(val isCompleted: Boolean) : TodoListAction()
    object ClearCompleted : TodoListAction()
    class Fetching(val isFetching: Boolean) : TodoListAction()
}

