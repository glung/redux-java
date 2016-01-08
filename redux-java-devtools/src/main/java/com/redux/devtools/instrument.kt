package com.redux.devtools

import com.redux.Store
import com.redux.Subscriber
import com.redux.createStore

// action creator
sealed class DevToolAction() {
    class PerformAction<AppAction>(val appAction: AppAction) : DevToolAction()

    class ResetAction : DevToolAction()

    class RollbackAction : DevToolAction()

    class CommitAction : DevToolAction()

    class SweepAction : DevToolAction()

    class ToggleAction(val index: Int) : DevToolAction()

    class JumpToStateAction(val index: Int) : DevToolAction()

}

// instrument
fun <AppAction, AppState> instrument(): ((AppAction, AppState) -> AppState, AppState) -> Store<AppAction, AppState> {

    data class DevToolState(
            val nextActionId: Int = 0,
            val currentStateIndex: Int = 0,
            val actionsById: Map<Int, DevToolAction.PerformAction<AppAction>> = emptyMap(), // TODO : INIT ?
            val stagedActionIds: List<Int> = listOf(0),
            val skippedActionIds: List<Int> = emptyList(),
            val committedState: AppState,
            val computedStates: List<AppState> = emptyList()
    )

    /**
     * Runs the reducer on invalidated actions to get a fresh computation log.
     */
    fun recomputeStates(reducer: (AppAction, AppState) -> AppState, devState: DevToolState): List<AppState> {

        val nextComputedStates = arrayListOf<AppState>()
        with(devState) {
            computedStates.mapIndexed { i, state ->
                val actionId: Int = stagedActionIds[i]
                val devAction = actionsById[actionId]!!

                val previousState = if (i > 1) nextComputedStates[i - 1] else committedState
                val shouldSkip = skippedActionIds.indexOf(actionId) > -1;
                val entry = if (shouldSkip) previousState else reducer(devAction.appAction, previousState)

                nextComputedStates.add(entry);
            }
        }
        return nextComputedStates;
    }

    /**
     * Lifts an app's action into an action on the lifted store.
     */
    fun liftAction(action: AppAction): DevToolAction.PerformAction<AppAction> =
            DevToolAction.PerformAction(action)

    /**
     * Creates a history state reducer from an app's reducer.
     */
    fun liftReducerWith(
            reducer: (AppAction, AppState) -> AppState,
            initialState: AppState): (DevToolAction, DevToolState) -> DevToolState {

        return { liftedAction, liftedState ->
            val updatedLiftedState = when (liftedAction) {
                is DevToolAction.ResetAction ->
                    DevToolState(committedState = initialState)
                is DevToolAction.CommitAction ->
                    liftedState.copy(
                            nextActionId = 0,
                            actionsById = emptyMap(),
                            stagedActionIds = listOf(0),
                            skippedActionIds = emptyList(),
                            committedState = liftedState.computedStates[liftedState.currentStateIndex],
                            currentStateIndex = 0,
                            computedStates = emptyList()
                    )
                is DevToolAction.RollbackAction ->
                    liftedState.copy(
                            nextActionId = 0,
                            actionsById = emptyMap(),
                            stagedActionIds = listOf(0),
                            skippedActionIds = emptyList(),
                            currentStateIndex = 0,
                            computedStates = emptyList()
                    )
                is DevToolAction.ToggleAction -> {
                    val updatedSkippedActions = with (liftedState.skippedActionIds) {
                        if (contains(liftedAction.index)) {
                            filter { it != liftedAction.index }
                        } else {
                            plus(liftedAction.index)
                        }
                    }

                    liftedState.copy(skippedActionIds = updatedSkippedActions)
                }
                is DevToolAction.JumpToStateAction ->
                    liftedState.copy(currentStateIndex = liftedAction.index)
                is DevToolAction.SweepAction -> {
                    val filteredStagedActionIds = liftedState.stagedActionIds.minus(liftedState.skippedActionIds)
                    liftedState.copy(
                            stagedActionIds = filteredStagedActionIds,
                            skippedActionIds = emptyList(),
                            currentStateIndex = Math.min(liftedState.currentStateIndex, filteredStagedActionIds.size - 1)
                    )
                }
                is DevToolAction.PerformAction<*> -> {
                    // Unfortunately, type is erased
                    val appAction = liftedAction.appAction as AppAction
                    liftedState.copy(
                            actionsById = liftedState.actionsById.plus(Pair(liftedState.nextActionId, DevToolAction.PerformAction(appAction)))
                    )
                }
            }

            val recomputedStates = recomputeStates(reducer, updatedLiftedState)
            updatedLiftedState.copy(computedStates = recomputedStates)
        }
    }

    fun liftState(appState: AppState) =
            DevToolState(committedState = appState)

    fun unliftState(liftedState: DevToolState) =
            liftedState.computedStates[liftedState.currentStateIndex]

    fun unliftStore(liftedStore: Store<DevToolAction, DevToolState>) = object : Store<AppAction, AppState> {
        override fun dispatch(action: AppAction) = liftedStore.dispatch(liftAction(action))

        override fun getState() = unliftState(liftedStore.getState())

        override fun subscribe(subscriber: Subscriber) = liftedStore.subscribe(subscriber)

        override fun unsubscribe(subscriber: Subscriber) = liftedStore.unsubscribe(subscriber)
    }

    return { reducer, initialState ->
        unliftStore(createStore(liftState(initialState), liftReducerWith(reducer, initialState)));
    }
}
