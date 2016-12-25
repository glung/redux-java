package com.redux

import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.Test as test

class StoreTest {

    @test fun actionShouldBeReduced() {

        val store = createStore<MyAction, MyState>(MyAction.INIT, MyState.EMPTY, { action: MyAction, state: MyState ->
            when (action) {
                MyAction.INIT -> MyState.EMPTY
                MyAction.ACTION -> MyState.REDUCED
            }
        })

        store.dispatch(MyAction.ACTION)

        assertEquals(MyState.REDUCED, store.getState())
    }

    @test fun storeShouldNotifySubscribers() {
        val store = createStore<MyAction, MyState>(MyAction.INIT, MyState.EMPTY) { action, state -> MyState.REDUCED }

        val subscriber1 = makeTestSubscriber()
        val subscriber2 = makeTestSubscriber()

        store.subscribe(subscriber1.subscriber)
        store.subscribe(subscriber2.subscriber)
        store.dispatch(MyAction.ACTION)

        assertTrue(subscriber1.called)
        assertTrue(subscriber2.called)
    }

    @test fun storeShouldNotNotifyWhenUnsubscribed() {
        val reducer = { action: MyAction, state: MyState -> MyState.REDUCED }
        val store = createStore<MyAction, MyState>(MyAction.INIT, MyState.EMPTY, reducer)
        val subscriber1 = makeTestSubscriber()
        val subscriber2 = makeTestSubscriber()

        store.subscribe(subscriber1.subscriber)
        store.subscribe(subscriber2.subscriber).unsubscribe()
        store.dispatch(MyAction.ACTION)

        assertTrue(subscriber1.called)
        assertFalse(subscriber2.called)
    }

    sealed class MyAction {
        object INIT : MyAction()

        object ACTION : MyAction()
    }

    sealed class MyState {
        object EMPTY : MyState()

        object REDUCED : MyState()
    }

    private fun makeTestSubscriber() =
            object {
                var called = false
                val subscriber = { -> called = true }
            }
}