package com.redux

import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.Test as test

class StoreTest {

    @test fun actionShouldBeReduced() {

        val store = Store<MyAction, MyState>(MyState.INIT) { action, state ->
            when (action) {
                MyAction.ACTION -> MyState.REDUCED
            }
        }

        store.dispatch(MyAction.ACTION)

        assertEquals(MyState.REDUCED, store.state)
    }

    @test fun storeShouldNotifySubscribers() {
        val store = Store<MyAction, MyState>(MyState.INIT) { action, state -> MyState.REDUCED }
        val subscriber1 = TestSubscriber()
        val subscriber2 = TestSubscriber()

        store.subscribe(subscriber1)
        store.subscribe(subscriber2)
        store.dispatch(MyAction.ACTION)

        assertTrue(subscriber1.called)
        assertTrue(subscriber2.called)
    }

    @test fun storeShouldNotNotifyWhenUnsubscribed() {
        val reducer = { action: MyAction, state: MyState -> MyState.REDUCED }
        val store = Store(MyState.INIT, reducer)
        val subscriber1 = TestSubscriber()
        val subscriber2 = TestSubscriber()

        store.subscribe(subscriber1)
        store.subscribe(subscriber2)
        store.unsubscribe(subscriber2)
        store.dispatch(MyAction.ACTION)

        assertTrue(subscriber1.called)
        assertFalse(subscriber2.called)
    }

    sealed class MyAction {
        object ACTION : MyAction()
    }

    sealed class MyState {
        object INIT : MyState()
        object REDUCED : MyState()
    }

    class TestSubscriber(var called : Boolean = false) : Subscriber {

        override fun onStateChanged() {
            called = true;
        }
    }
}