package com.redux.devtools;


import com.redux.Reducer;
import com.redux.Store;

import java.io.File;
import java.util.Collections;

public class DevTool<A, S> {
    private final Store<DevToolAction<A>, DevToolState<A, S>> devStore;
    private final Store<A, S> appStore;

    public DevTool(Store<DevToolAction<A>, DevToolState<A, S>> devStore, Store<A, S> appStore) {
        this.devStore = devStore;
        this.appStore = appStore;
    }

    public Store<DevToolAction<A>, DevToolState<A, S>> getDevToolsStore() {
        return devStore;
    }

    public Store<A, S> getApplicationStore() {
        return appStore;
    }

    public static class Builder<A, S> {

        private final Reducer<A, S> reducer;
        private final A initAction;
        private final S initialState;

        private DevToolStateStorage<A, S> storage;
        private ApplicationStateMonitor<S> applicationStateMonitor;

        public Builder(A initAction, S initialState, Reducer<A, S> reducer) {
            this.initAction = initAction;
            this.initialState = initialState;
            this.reducer = reducer;
        }

        public <X> Builder<A, S> withMonitor(ApplicationStateMonitor<S> applicationStateMonitor) {
            this.applicationStateMonitor = applicationStateMonitor;
            return this;
        }

        public Builder<A, S> withSessionPersistence(File fileStorage,
                                                    Converter<A> actionConverter,
                                                    Converter<S> stateConverter) {
            storage = new DevToolStateStorage<>(fileStorage, actionConverter, stateConverter);
            return this;
        }

        public DevTool<A, S> create() {
            final Store<DevToolAction<A>, DevToolState<A, S>> devStore = createDevStore(applicationStateMonitor, storage);
            final Store<A, S> prodStore = unlift(devStore);

            return new DevTool<>(devStore, prodStore);
        }

        private Store<A, S> unlift(final Store<DevToolAction<A>, DevToolState<A, S>> liftedStore) {
            return null;
//            return new Store<A, S>() {
//
//                @Override public void dispatch(A action) {
//                    liftedStore.dispatch(DevToolAction.forPerformAction(action));
//                }
//
//                @Override public Subscription subscribe(final Subscriber subscriber) {
//                    return liftedStore.subscribe(subscriber);
//                }
//
//                @Override public S getState() {
//                    return unlift(liftedStore.getState());
//                }
//
//                private S unlift(DevToolState<A, S> state) {
//                    return state.computedStates.get(state.currentStateIndex);
//                }
//            };
        }

        private Store<DevToolAction<A>, DevToolState<A, S>> createDevStore(final ApplicationStateMonitor<S> applicationStateMonitor,
                                                                           final DevToolStateStorage<A, S> storage) {

            return null;
//            final DevToolState<A, S> initialDevToolState = getInitialDevToolState(storage);
//            final DevToolReducer<A, S> liftedReducer = new DevToolReducer<>(initAction, initialState, reducer);
//            final Store<DevToolAction<A>, DevToolState<A, S>> liftedStore = Store.create(initialDevToolState, liftedReducer);
//
//            return new Store<DevToolAction<A>, DevToolState<A, S>>() {
//
//                @Override public Subscription subscribe(Subscriber subscriber) {
//                    return liftedStore.subscribe(subscriber);
//                }
//
//                @Override public DevToolState<A, S> getState() {
//                    return liftedStore.getState();
//                }
//
//                @Override public void dispatch(DevToolAction<A> action) {
//                    liftedStore.dispatch(action);
//
//                    if (applicationStateMonitor != null) {
//                        applicationStateMonitor.print(getCurrentState());
//                    }
//                    if (storage != null) {
//                        storage.store(getState());
//                    }
//                }
//
//                private S getCurrentState() {
//                    return getState().computedStates.get(getState().currentStateIndex);
//                }
//            };
        }

        private DevToolState<A, S> getInitialDevToolState(DevToolStateStorage<A, S> storage) {
            if (storage != null) {
                final DevToolState<A, S> parsed = storage.load();
                if (parsed != null) {
                    return parsed;
                }
            }
            return createInitialState();
        }

        private DevToolState<A, S> createInitialState() {
            DevToolState<A, S> liftedInitialState;
            liftedInitialState = new DevToolState<>(
                    System.currentTimeMillis(),
                    initialState,
                    Collections.singletonList(initAction),
                    Collections.<Integer>emptyList(),
                    Collections.singletonList(initialState),
                    Collections.singletonList(0L),
                    Collections.singletonList(0L),
                    0
            );
            return liftedInitialState;
        }

    }

}
