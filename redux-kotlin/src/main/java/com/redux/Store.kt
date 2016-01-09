package com.redux

import java.util.concurrent.atomic.AtomicBoolean

interface Store<Action, State> {
    val state: State

    fun subscribe(subscriber: Subscriber): Boolean
    fun unsubscribe(subscriber: Subscriber): Boolean
    fun dispatch(action: Action): Unit
}

//TODO : init ?
fun <Action, State> createStore(initialState: State, reducer: (Action, State) -> State): Store<Action, State> {
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

    return object : Store<Action, State> {
        override var state = initialState
        val subscribers = arrayListOf<Subscriber>()
        val safeReducer = createSafeReducer(reducer)

        override fun subscribe(subscriber: Subscriber) = subscribers.add(subscriber)

        override fun unsubscribe(subscriber: Subscriber) = subscribers.remove(subscriber)

        override fun dispatch(action: Action) {
            state = safeReducer(action, state)
            notifyStateChanged()
        }

        private fun notifyStateChanged() = subscribers.forEach { it.onStateChanged() }

    }
}
