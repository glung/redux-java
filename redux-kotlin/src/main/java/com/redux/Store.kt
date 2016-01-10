package com.redux

import java.util.concurrent.atomic.AtomicBoolean

interface Subscription {
    fun unsubscribe()
}

interface Store<Action, State> {
    fun subscribe(subscriber: () -> Unit): Subscription
    fun dispatch(action: Action): Unit
    fun getState(): State
}

fun <Action, State> createStore(
        initAction: Action,
        initialState: State,
        reducer: (Action, State) -> State): Store<Action, State> {
    fun createSafeReducer(reducer: (Action, State) -> State): (Action, State) -> State {
        val isReducing = AtomicBoolean(false)

        return { action, state ->
            checkState(!isReducing.get(), "Can not dispatch an action when an other action is being processed")

            isReducing.set(true)
            val reducedState = reducer(action, state)
            isReducing.set(false)

            reducedState
        }
    }

    val store = object : Store<Action, State> {
        var _state = initialState
        val subscribers : MutableList<() -> Unit> = arrayListOf()
        val safeReducer = createSafeReducer(reducer)

        override fun subscribe(subscriber: () -> Unit): Subscription {
            subscribers.add(subscriber)

            return object : Subscription {
                override fun unsubscribe() {
                    subscribers.remove(subscriber)
                }
            }
        }

        override fun dispatch(action: Action) {
            _state = safeReducer(action, _state)
            notifyStateChanged()
        }

        fun notifyStateChanged() {
            subscribers.forEach { it() }
        }

        override fun getState() = _state

    }
    store.dispatch(initAction);
    return store
}
