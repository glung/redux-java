package com.redux.devtools

import com.redux.Store

// action creator
sealed class DevToolAction() {
    class PerformAppAction<AppAction>(val appAction: AppAction) : DevToolAction()

    object Reset : DevToolAction()

    object Rollback : DevToolAction()

    object Commit : DevToolAction()

    object Sweep : DevToolAction()

    class Toggle(val index: Int) : DevToolAction()

    class JumpToState(val index: Int) : DevToolAction()

}

data class DevToolState<AppAction, AppState>(
        val nextActionId: Int = 0,
        val currentStateIndex: Int = -1,
        val actionsById: Map<Int, DevToolAction.PerformAppAction<AppAction>> = emptyMap(),
        val stagedActionIds: List<Int> = emptyList(),
        val skippedActionIds: List<Int> = emptyList(),
        val committedState: AppState,
        val computedStates: List<AppState> = emptyList()
)

// A = AppAction
// S = AppState
class DevTools<A, S> {
    lateinit var liftedStore: Store<DevToolAction, DevToolState<A, S>>

    // createStore -> state, reducer -> store
    // createStore =   (DevToolAction, DevToolState<A, S>, (DevToolAction, DevToolState<A, S>) -> DevToolState<A, S>) -> Store<DevToolAction, DevToolState<A, S>>
    fun instrument(): ((DevToolAction, DevToolState<A, S>, (DevToolAction, DevToolState<A, S>) -> DevToolState<A, S>) -> Store<DevToolAction, DevToolState<A, S>>) -> (A, S, (A, S) -> S) -> Store<A, S> {
        /**
         * Runs the reducer on invalidated actions to get a fresh computation log.
         */
        fun recomputeStates(reducer: (A, S) -> S, devState: DevToolState<A, S>): List<S> {

            val nextComputedStates = arrayListOf<S>()
            with(devState) {
                for ((i, actionId) in stagedActionIds.withIndex()) {
                    val devAction = actionsById[actionId]!!

                    val previousState = if (i > 1) nextComputedStates[i - 1] else committedState
                    val shouldSkip = skippedActionIds.indexOf(i) > -1
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
                initAction: A,
                initialState: S,
                reducer: (A, S) -> S): (DevToolAction, DevToolState<A, S>) -> DevToolState<A, S> {

            return { liftedAction, liftedState ->
                val updatedLiftedState = when (liftedAction) {
                    is DevToolAction.Reset ->
                        DevToolState (
                                nextActionId = 1,
                                actionsById = mapOf(Pair(0, liftAction(initAction))),
                                committedState = initialState
                        )
                    is DevToolAction.Commit ->
                        liftedState.copy(
                                nextActionId = 1,
                                actionsById = mapOf(Pair(0, liftAction(initAction))),
                                stagedActionIds = listOf(0),
                                skippedActionIds = emptyList(),
                                committedState = liftedState.computedStates[liftedState.currentStateIndex],
                                currentStateIndex = 0,
                                computedStates = emptyList()
                        )
                    is DevToolAction.Rollback ->
                        liftedState.copy(
                                nextActionId = 1,
                                actionsById = mapOf(Pair(0, liftAction(initAction))),
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
                        val appAction = (liftedAction.appAction as A)!!
                        liftedState.copy(
                                currentStateIndex = liftedState.currentStateIndex + 1, // TODO : not always increment
                                nextActionId = liftedState.nextActionId + 1,
                                stagedActionIds = liftedState.stagedActionIds.plus(liftedState.nextActionId),
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

            override fun getState() = unliftState(liftedStore.getState())

            override fun subscribe(subscriber: () -> Unit) = liftedStore.subscribe(subscriber)
        }

        return {
            createStore ->
            { initAction, state, reducer ->
                liftedStore = createStore(liftAction(initAction), liftState(state), liftReducerWith(initAction, state, reducer))
                unliftStore(liftedStore)
            }
        }
    }
}