package com.glung.redux;

import redux.api.Store;

import java.io.PrintStream;

class ReduxApplication {
    private final redux.api.Store<Integer> store;
    private final PrintStream stream;

    ReduxApplication(redux.api.Store.Creator storeCreator, PrintStream stream) {
        store = storeCreator.create(new MyReducer(), 0);
        this.stream = stream;
    }

    void runDemo() {
        store.subscribe(new MySubscriber(store, stream));
        store.dispatch(Action.INCREMENT); // print 1
        store.dispatch(Action.DECREMENT); // print 0
        store.dispatch("unknown action"); // print 0
        store.dispatch(Action.INCREMENT); // print 1
    }

    private static class MySubscriber implements redux.api.Store.Subscriber {
        private final redux.api.Store<Integer> store;
        private final PrintStream stream;

        private MySubscriber(Store<Integer> store, PrintStream stream) {
            this.store = store;
            this.stream = stream;
        }

        @Override
        public void onStateChanged() {
            stream.println(store.getState());
        }
    }
}
