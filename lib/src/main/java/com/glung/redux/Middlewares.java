package com.glung.redux;

import java.util.Arrays;
import java.util.List;

import redux.api.*;
import redux.api.Store;
import redux.api.enhancer.Middleware;

public class Middlewares {

    private static class MiddlewareStore<S> implements redux.api.Store<S> {

        private final redux.api.Store<S> nextStore;
        private final List<redux.api.enhancer.Middleware<S>> middlewares;

        public MiddlewareStore(redux.api.Store<S> nextStore,
                List<redux.api.enhancer.Middleware<S>> middlewares) {
            this.nextStore = nextStore;
            this.middlewares = middlewares;
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
            return createMiddlewareDispatcher(middlewares).dispatch(action);
        }

        private Dispatcher createMiddlewareDispatcher(final List<redux.api.enhancer.Middleware<S>> middlewares) {
            if (middlewares.isEmpty()) {
                return nextStore;
            } else {
                return new Dispatcher() {
                    @Override
                    public Object dispatch(Object action) {
                        Middleware<S> nextMiddleware = middlewares.get(0);
                        List<redux.api.enhancer.Middleware<S>> restMiddlewares = middlewares.subList(1, middlewares.size());
                        return  nextMiddleware.dispatch(nextStore, createMiddlewareDispatcher(restMiddlewares), action);
                    }
                };

            }
        }
    }

    @SafeVarargs
    public static <S> redux.api.Store.Enhancer<S> applyMiddlewares(final redux.api.enhancer.Middleware<S>... middlewares) {
        return new redux.api.Store.Enhancer<S>(){
            @Override
            public Store.Creator<S> enhance(final Store.Creator<S> next) {
                return new Store.Creator<S>() {
                    @Override
                    public Store<S> create(final Reducer<S> reducer,final S initialState) {
                        Store<S> store = next.create(reducer, initialState);
                        return new MiddlewareStore<>(store, Arrays.asList(middlewares));
                    }
                };
            }
        };
    }
}
