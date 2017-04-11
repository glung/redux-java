package com.glung.redux;

import redux.api.Dispatcher;
import redux.api.Reducer;
import redux.api.Store;
import redux.api.enhancer.Middleware;

import java.util.Arrays;
import java.util.List;

public class Middlewares {

    private static class MiddlewareStore<S> implements redux.api.Store<S> {

        private final Dispatcher dispatcher;
        private final Store<S> nextStore;

        private MiddlewareStore(Store<S> nextStore,
                                Dispatcher dispatcher) {
            this.nextStore = nextStore;
            this.dispatcher = dispatcher;
        }

        @Override
        public S getState() {
            return nextStore.getState();
        }

        @Override
        public Subscription subscribe(Subscriber subscriber) {
            return nextStore.subscribe(subscriber);
        }

        @Override
        public void replaceReducer(Reducer<S> reducer) {
            nextStore.replaceReducer(reducer);
        }

        @Override
        public Object dispatch(Object action) {
            return dispatcher.dispatch(action);
        }

    }

    public static <T> Store.Enhancer applyMiddlewares(final Middleware<T>... middlewares) {
        return new Store.Enhancer() {
            @Override
            public Store.Creator enhance(final Store.Creator next) {
                return new Store.Creator() {
                    @Override
                    public <S> Store<S> create(final Reducer<S> reducer, final S initialState) {
                        final Store<S> store = next.create(reducer, initialState);
                        // This is fqr from ideal but currently it is expected that the Middleware and the Reducer carry the same type.
                        // This is checked at run time (cf down cast below)
                        //
                        // Revisit when needed
                        return new MiddlewareStore<>(store, createMiddlewareDispatcher(Arrays.asList(middlewares), (Store<T>) store));
                    }
                };
            }
        };
    }

    private static <S> Dispatcher createMiddlewareDispatcher(final List<Middleware<S>> middlewares, final Store<S> nextStore) {
        Dispatcher currentDispatcher = nextStore;
        for (int i = middlewares.size() - 1; i >= 0; i--) {
            final Middleware<S> nextMiddleware = middlewares.get(i);
            currentDispatcher = createNextDispatcher(nextStore, currentDispatcher, nextMiddleware);
        }
        return currentDispatcher;
    }

    private static <S> Dispatcher createNextDispatcher(final Store<S> nextStore, final Dispatcher lastDispatcher, final Middleware<S> nextMiddleware) {
        return new Dispatcher() {
            @Override
            public Object dispatch(Object action) {
                return nextMiddleware.dispatch(nextStore, lastDispatcher, action);
            }
        };
    }
}
