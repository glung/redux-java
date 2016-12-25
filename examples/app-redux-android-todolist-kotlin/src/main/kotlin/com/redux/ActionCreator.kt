package com.redux

import javax.inject.Inject

public class ActionCreator @Inject constructor(
        private val store: Store<TodoListAction, TodoListState>,
        private val operations: Operations) {

    public fun fetch(): rx.Subscription {
        store.dispatch(TodoListAction.Fetching(true))
        return operations
                .fetch()
                .doOnNext { todos -> todos.forEach { store.dispatch(TodoListAction.Add(it.text, it.isCompleted)) } }
                .doOnCompleted { store.dispatch(TodoListAction.Fetching(false)) }
                .doOnError { store.dispatch(TodoListAction.Fetching(false)) }
                .subscribe()
    }

    public fun add(text: String) = store.dispatch(TodoListAction.Add(text, false))

    public fun add(text: String, isCompleted: Boolean) = store.dispatch(TodoListAction.Add(text, isCompleted))

    public fun delete(id: Int) = store.dispatch(TodoListAction.Delete(id))

    public fun complete(id: Int, isCompleted: Boolean) = store.dispatch(TodoListAction.Complete(id, isCompleted))

    public fun completeAll(isCompleted: Boolean) = store.dispatch(TodoListAction.CompleteAll(isCompleted))

    public fun clearCompleted() = store.dispatch(TodoListAction.ClearCompleted)

}
