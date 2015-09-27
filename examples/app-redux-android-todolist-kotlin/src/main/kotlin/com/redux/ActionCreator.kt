package com.redux

import javax.inject.Inject

public class ActionCreator @Inject constructor(
        private val store: Store<AppAction, AppState>,
        private val operations: Operations) {

    public fun fetch(): rx.Subscription {
        store.dispatch(AppAction.Fetching(true))
        return operations
                .fetch()
                .doOnNext { todos -> todos.forEach { store.dispatch(AppAction.Add(it.text, it.isCompleted)) } }
                .doOnCompleted { store.dispatch(AppAction.Fetching(false)) }
                .doOnError { store.dispatch(AppAction.Fetching(false)) }
                .subscribe()
    }

    public fun add(text: String) = store.dispatch(AppAction.Add(text, false))

    public fun add(text: String, isCompleted: Boolean) = store.dispatch(AppAction.Add(text, isCompleted))

    public fun delete(id: Int) = store.dispatch(AppAction.Delete(id))

    public fun complete(id: Int, isCompleted: Boolean) = store.dispatch(AppAction.Complete(id, isCompleted))

    public fun completeAll(isCompleted: Boolean) = store.dispatch(AppAction.CompleteAll(isCompleted))

    public fun clearCompleted() = store.dispatch(AppAction.ClearCompleted)

}
