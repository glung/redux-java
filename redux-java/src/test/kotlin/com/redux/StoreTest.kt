package com.redux

import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.Test as test

class StoreTest {

    @test fun actionShouldBeReduced() {
        val reducer = Reducer { action: MyAction, state: MyState ->
            when (action.type) {
                "to reduce" -> MyState("reduced")
                else -> state
            }
        }

        val store = Store.create(MyState(), reducer)
        store.dispatch(MyAction(type = "to reduce"))

        assertEquals("reduced", store.getState().state)
    }

    @test fun storeShouldNotifySubscribers() {
        val store = Store.create(MyState(), Reducer { action: MyAction, state: MyState ->  MyState()})
        val subscriber1 = TestSubscriber()
        val subscriber2 = TestSubscriber()

        store.subscribe(subscriber1)
        store.subscribe(subscriber2)
        store.dispatch(MyAction())

        assertTrue(subscriber1.called)
        assertTrue(subscriber2.called)
    }


    @test fun storeShouldNotNotifyWhenUnsubscribed() {
        val store = Store.create(MyState(), Reducer { action: MyAction, state: MyState ->  MyState()})
        val subscriber1 = TestSubscriber()
        val subscriber2 = TestSubscriber()

        store.subscribe(subscriber1)
        val subscription = store.subscribe(subscriber2)
        subscription.unsubscribe()
        store.dispatch(MyAction())

        assertTrue(subscriber1.called)
        assertFalse(subscriber2.called)
    }


    data class MyAction(val type: String = "unknown") : Action

    data class MyState(val state: String = "initial state") : State

    class TestSubscriber : Subscriber {
        var called = false;

        override fun onStateChanged() {
            called = true;
        }
    }
}