package com.redux;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static mini.com.google.common.base.Preconditions.checkState;

public abstract class Store<A extends Action, S extends State> {

    static public <A extends Action, S extends State> CoreStore<A, S> create(S initialState, Reducer<A, S> reducer) {
        return new CoreStore<>(initialState, reducer);
    }

    public abstract Subscription subscribe(Subscriber subscriber);

    public abstract S getState();

    public abstract void dispatch(A action);

    static class CoreStore<A extends Action, S extends State> extends Store<A,S> {

        private static final int LISTENERS_INITIAL_CAPACITY = 100;

        private final List<Subscriber> subscribers;
        private final Reducer<A, S> reducer;
        private final AtomicBoolean isReducing;

        private S currentState;

        CoreStore(S initialState, Reducer<A, S> reducer) {
            this.reducer = reducer;
            this.currentState = initialState;
            this.subscribers = new ArrayList<>(LISTENERS_INITIAL_CAPACITY);
            this.isReducing = new AtomicBoolean(false);
        }

        @Override public Subscription subscribe(Subscriber subscriber) {
            subscribers.add(subscriber);
            return new Subscription<>(subscribers, subscriber);
        }

        @Override public S getState() {
            return currentState;
        }

        @Override public void dispatch(final A action) {
            checkState(!isReducing.get(), "Can not dispatch an action when an other action is being processed");

            isReducing.set(true);
            currentState = reduce(action, currentState);
            isReducing.set(false);

            notifyStateChanged();
        }

        private S reduce(A action, S state) {
            return reducer.call(action, state);
        }

        private void notifyStateChanged() {
            for (int i = 0, size = subscribers.size(); i < size; i++) {
                subscribers.get(i).onStateChanged();
            }
        }
    }
}
