package glung.redux_java2;

import redux.api.Reducer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

class Store<S> implements redux.api.Store<S> {

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
    public Subscription subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
        return () -> subscribers.remove(subscriber);
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
        checkIsNotReducing();
        currentState = reduce(action);
        new ArrayList<>(subscribers).forEach(Subscriber::onStateChanged);
        return action;
    }

    private void checkIsNotReducing() {
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

    static class Creator<S> implements redux.api.Store.Creator<S> {

        @Override
        public redux.api.Store create(Reducer<S> reducer, S initialState) {
            return new Store<S>(reducer, initialState);
        }
    }
}
