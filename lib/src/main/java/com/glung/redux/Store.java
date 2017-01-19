package com.glung.redux;

import redux.api.Reducer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Store<S> implements redux.api.Store<S> {

    private final AtomicBoolean isReducing = new AtomicBoolean(false);
    private final List<Subscriber> subscribers = new ArrayList<>();

    private Reducer<S> reducer;
    private S currentState;

    Store(Reducer<S> reducer, S initialState) {
        this.currentState = initialState;
        setReducer(reducer);
    }

    @Override
    public S getState() {
        return currentState;
    }

    @Override
    public Subscription subscribe(final Subscriber subscriber) {
        subscribers.add(subscriber);
        return new Subscription() {
            @Override
            public void unsubscribe() {
                subscribers.remove(subscriber);
            }
        };
    }

    @Override
    public void replaceReducer(Reducer<S> reducer) {
        setReducer(reducer);
    }

    private void setReducer(Reducer<S> reducer) {
        this.reducer = reducer;
        this.reducer.reduce(currentState, redux.api.Store.INIT);
    }

    @Override
    public Object dispatch(Object action) {
        assertIsNotReducing();

        currentState = reduce(action);
        notifySubscribers();
        return action;
    }

    private void notifySubscribers() {
        for (Subscriber subscriber : new ArrayList<>(subscribers)) {
            subscriber.onStateChanged();
        }
    }

    private void assertIsNotReducing() {
        if (isReducing.get()) {
            throw new IllegalStateException("Already reducing");
        }
    }

    private S reduce(Object action) {
        startReducing();
        final S reducedSate = reducer.reduce(currentState, action);
        stopReducing();
        return reducedSate;
    }


    private void stopReducing() {
        isReducing.set(false);
    }

    private void startReducing() {
        isReducing.set(true);
    }

    public static class Creator implements redux.api.Store.Creator {

        @Override
        public <S> redux.api.Store<S> create(Reducer<S> reducer, S initialState) {
            return new Store<>(reducer, initialState);
        }
    }
}
