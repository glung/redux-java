package com.redux.devtools

import com.redux.Store
import com.redux.Subscriber

// action creator
sealed class DevToolAction() {
    class PerformAppAction<AppAction>(val appAction: AppAction) : DevToolAction()

    class Reset : DevToolAction()

    class Rollback : DevToolAction()

    class Commit : DevToolAction()

    class Sweep : DevToolAction()

    class Toggle(val index: Int) : DevToolAction()

    class JumpToState(val index: Int) : DevToolAction()

}

data class DevToolState<AppAction, AppState>(
        val nextActionId: Int = 0,
        val currentStateIndex: Int = 0,
        val actionsById: Map<Int, DevToolAction.PerformAppAction<AppAction>> = emptyMap(), // TODO : INIT ?
        val stagedActionIds: List<Int> = listOf(0),
        val skippedActionIds: List<Int> = emptyList(),
        val committedState: AppState,
        val computedStates: List<AppState> = emptyList()
)

// A = AppAction
// S = AppState
class DevTools<A, S> {
    lateinit var liftedStore: Store<DevToolAction, DevToolState<A, S>>

    // createStore -> state, reducer -> store
    // createStore = (DevToolState<A, S>, (DevToolAction, DevToolState<A, S>) -> DevToolState<A, S>) -> Store<DevToolAction, DevToolState<A, S>>
    fun instrument(): ((DevToolState<A, S>, (DevToolAction, DevToolState<A, S>) -> DevToolState<A, S>) -> Store<DevToolAction, DevToolState<A, S>>) -> (S, (A, S) -> S) -> Store<A, S> {
        /**
         * Runs the reducer on invalidated actions to get a fresh computation log.
         */
        fun recomputeStates(reducer: (A, S) -> S, devState: DevToolState<A, S>): List<S> {

            val nextComputedStates = arrayListOf<S>()
            with(devState) {
                computedStates.mapIndexed { i, state ->
                    val actionId: Int = stagedActionIds[i]
                    val devAction = actionsById[actionId]!!

                    val previousState = if (i > 1) nextComputedStates[i - 1] else committedState
                    val shouldSkip = skippedActionIds.indexOf(actionId) > -1
                    val entry = if (shouldSkip) previousState else reducer(devAction.appAction, previousState)

                    nextComputedStates.add(entry);
                }
            }
            return nextComputedStates;
        }

        /**
         * Lifts an app's action into an action on the lifted store.
         */
        fun liftAction(action: A): DevToolAction.PerformAppAction<A> =
                DevToolAction.PerformAppAction(action)

        /**
         * Creates a history state reducer from an app's reducer.
         */
        fun liftReducerWith(
                reducer: (A, S) -> S,
                initialState: S): (DevToolAction, DevToolState<A, S>) -> DevToolState<A, S> {

            return { liftedAction, liftedState ->
                val updatedLiftedState = when (liftedAction) {
                    is DevToolAction.Reset ->
                        DevToolState<A, S> (committedState = initialState)
                    is DevToolAction.Commit ->
                        liftedState.copy(
                                nextActionId = 0,
                                actionsById = emptyMap(),
                                stagedActionIds = listOf(0),
                                skippedActionIds = emptyList(),
                                committedState = liftedState.computedStates[liftedState.currentStateIndex],
                                currentStateIndex = 0,
                                computedStates = emptyList()
                        )
                    is DevToolAction.Rollback ->
                        liftedState.copy(
                                nextActionId = 0,
                                actionsById = emptyMap(),
                                stagedActionIds = listOf(0),
                                skippedActionIds = emptyList(),
                                currentStateIndex = 0,
                                computedStates = emptyList()
                        )
                    is DevToolAction.Toggle -> {
                        val updatedSkippedActions = with (liftedState.skippedActionIds) {
                            if (contains(liftedAction.index)) {
                                filter { it != liftedAction.index }
                            } else {
                                plus(liftedAction.index)
                            }
                        }

                        liftedState.copy(skippedActionIds = updatedSkippedActions)
                    }
                    is DevToolAction.JumpToState ->
                        liftedState.copy(currentStateIndex = liftedAction.index)
                    is DevToolAction.Sweep -> {
                        val filteredStagedActionIds = liftedState.stagedActionIds.minus(liftedState.skippedActionIds)
                        liftedState.copy(
                                stagedActionIds = filteredStagedActionIds,
                                skippedActionIds = emptyList(),
                                currentStateIndex = Math.min(liftedState.currentStateIndex, filteredStagedActionIds.size - 1)
                        )
                    }
                    is DevToolAction.PerformAppAction<*> -> {
                        // Unfortunately, type is erased
                        val appAction = liftedAction.appAction as A
                        liftedState.copy(
                                actionsById = liftedState.actionsById.plus(Pair(liftedState.nextActionId, DevToolAction.PerformAppAction(appAction)))
                        )
                    }
                }

                val recomputedStates = recomputeStates(reducer, updatedLiftedState)
                updatedLiftedState.copy(computedStates = recomputedStates)
            }
        }

        fun liftState(appState: S) =
                DevToolState<A, S> (committedState = appState)

        fun unliftState(liftedState: DevToolState<A, S>) =
                liftedState.computedStates[liftedState.currentStateIndex]

        fun unliftStore(liftedStore: Store<DevToolAction, DevToolState<A, S>>) = object : Store<A, S> {
            override fun dispatch(action: A) = liftedStore.dispatch(liftAction(action))

            override var state = unliftState(liftedStore.state)
                get() = unliftState(liftedStore.state)

            override fun subscribe(subscriber: Subscriber) = liftedStore.subscribe(subscriber)

            override fun unsubscribe(subscriber: Subscriber) = liftedStore.unsubscribe(subscriber)
        }

        return {
            createStore -> { state, reducer ->
                liftedStore = createStore(liftState(state), liftReducerWith(reducer, state))
                unliftStore(liftedStore)
            }
        }
    }
}