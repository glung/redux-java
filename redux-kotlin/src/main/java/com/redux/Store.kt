package com.redux

import java.util.concurrent.atomic.AtomicBoolean

interface Store<Action, State> {
    fun subscribe(subscriber: Subscriber): Boolean
    fun unsubscribe(subscriber: Subscriber): Boolean
    fun dispatch(action: Action): Unit
    fun getState(): State
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
        val subscribers = arrayListOf<Subscriber>()
        val _reducer = createSafeReducer(reducer) // use _ to avoid name clash
        var _state = initialState; // use _ to avoid name clash

        override fun subscribe(subscriber: Subscriber) = subscribers.add(subscriber)

        override fun unsubscribe(subscriber: Subscriber) = subscribers.remove(subscriber)

        override fun dispatch(action: Action) {
            _state = _reducer(action, _state)
            notifyStateChanged()
        }

        override fun getState() = _state

        private fun notifyStateChanged() = subscribers.forEach { it.onStateChanged() }

    }
}
