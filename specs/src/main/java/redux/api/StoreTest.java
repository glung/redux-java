package redux.api;

import org.junit.Test;
import redux.api.helpers.Reducers;
import redux.api.helpers.State;
import redux.api.helpers.Todo;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static redux.api.helpers.ActionsCreator.addTodo;
import static redux.api.helpers.ActionsCreator.unknownAction;

public abstract class StoreTest {

    public abstract <S> Store<S> createStore(Reducer<S> reducer, S state);

    @Test
    public void passesTheInitialActionAndTheInitialState() {
        final State initialState = new State(new Todo(1, "Hello"));
        final Store<State> store = createStore(Reducers.TODOS, initialState);

        assertThat(store.getState()).isEqualTo(initialState);
    }

    @Test
    public void appliesTheReducerToThePreviousState() {
        final Store<State> store = createStore(Reducers.TODOS, new State());
        assertThat(store.getState()).isEqualTo(new State());

        store.dispatch(unknownAction());
        assertThat(store.getState()).isEqualTo(new State());

        store.dispatch(addTodo("Hello"));
        assertThat(store.getState()).isEqualTo(new State(new Todo(1, "Hello")));

        store.dispatch(addTodo("World"));
        assertThat(store.getState()).isEqualTo(new State(
                new Todo(1, "Hello"),
                new Todo(2, "World")
        ));
    }


    @Test
    public void appliesTheReducerToTheInitialState() {
        final Store<State> store = createStore(Reducers.TODOS, new State(new Todo(1, "Hello")));

        assertThat(store.getState()).isEqualTo(new State(new Todo(1, "Hello")));

        store.dispatch(unknownAction());
        assertThat(store.getState()).isEqualTo(new State(new Todo(1, "Hello")));

        store.dispatch(addTodo("World"));
        assertThat(store.getState()).isEqualTo(new State(
                new Todo(1, "Hello"),
                new Todo(2, "World"))
        );
    }

    @Test
    public void sendsInitWhenReplacingAReducer() {
        final Store<State> store = createStore(Reducers.TODOS, new State());

        final StubbedStateReducer reducer = new StubbedStateReducer();
        store.replaceReducer(reducer);
        assertThat(reducer.receivedAction).isEqualTo(Store.INIT);
    }

    @Test
    public void preservesTheStateWhenReplacingAReducer() {
        final Store<State> store = createStore(Reducers.TODOS, new State());
        store.dispatch(addTodo("Hello"));
        store.dispatch(addTodo("World"));
        assertThat(store.getState()).isEqualTo(new State(
                new Todo(1, "Hello"),
                new Todo(2, "World"))
        );

        store.replaceReducer(Reducers.TODOS_REVERSE);
        assertThat(store.getState()).isEqualTo(new State(
                new Todo(1, "Hello"),
                new Todo(2, "World"))
        );

        store.dispatch(addTodo("Perhaps"));
        assertThat(store.getState()).isEqualTo(new State(
                new Todo(3, "Perhaps"),
                new Todo(1, "Hello"),
                new Todo(2, "World"))
        );

        store.replaceReducer(Reducers.TODOS);
        assertThat(store.getState()).isEqualTo(new State(
                new Todo(3, "Perhaps"),
                new Todo(1, "Hello"),
                new Todo(2, "World"))
        );

        store.dispatch(addTodo("Surely"));
        assertThat(store.getState()).isEqualTo(new State(
                new Todo(3, "Perhaps"),
                new Todo(1, "Hello"),
                new Todo(2, "World"),
                new Todo(4, "Surely"))
        );
    }

    @Test
    public void supportsMultipleSubscriptions() {
        final Store<State> store = createStore(Reducers.TODOS, new State());
        final Store.Subscriber subscriberA = mock(Store.Subscriber.class);
        final Store.Subscriber subscriberB = mock(Store.Subscriber.class);
        final Store.Subscription subscriptionA = store.subscribe(subscriberA);

        store.dispatch(unknownAction());
        verify(subscriberA, times(1)).onStateChanged();
        verify(subscriberB, times(0)).onStateChanged();

        store.dispatch(unknownAction());
        verify(subscriberA, times(2)).onStateChanged();
        verify(subscriberB, times(0)).onStateChanged();

        final Store.Subscription subscriptionB = store.subscribe(subscriberB);
        store.dispatch(unknownAction());
        verify(subscriberA, times(3)).onStateChanged();
        verify(subscriberB, times(1)).onStateChanged();

        subscriptionA.unsubscribe();
        verify(subscriberA, times(3)).onStateChanged();
        verify(subscriberB, times(1)).onStateChanged();

        store.dispatch(unknownAction());
        verify(subscriberA, times(3)).onStateChanged();
        verify(subscriberB, times(2)).onStateChanged();

        subscriptionB.unsubscribe();
        verify(subscriberA, times(3)).onStateChanged();
        verify(subscriberB, times(2)).onStateChanged();

        store.dispatch(unknownAction());
        verify(subscriberA, times(3)).onStateChanged();
        verify(subscriberB, times(2)).onStateChanged();

        store.subscribe(subscriberA);
        verify(subscriberA, times(3)).onStateChanged();
        verify(subscriberB, times(2)).onStateChanged();

        store.dispatch(unknownAction());
        verify(subscriberA, times(4)).onStateChanged();
        verify(subscriberB, times(2)).onStateChanged();
    }

    @Test
    public void onlyRemovesSubscriberOnceWhenUnsubscribeIsCalled() {
        final Store<State> store = createStore(Reducers.TODOS, new State());
        final Store.Subscriber subscriberA = mock(Store.Subscriber.class);
        final Store.Subscriber subscriberB = mock(Store.Subscriber.class);

        final Store.Subscription subscriptionA = store.subscribe(subscriberA);
        store.subscribe(subscriberB);
        subscriptionA.unsubscribe();
        subscriptionA.unsubscribe();

        store.dispatch(unknownAction());

        verify(subscriberA, times(0)).onStateChanged();
        verify(subscriberB, times(1)).onStateChanged();
    }

    @Test
    public void onlyRemovesRelevantSubscriberWhenUnsubscribeIsCalled() {
        final Store<State> store = createStore(Reducers.TODOS, new State());
        final SubbedSubscriber subscriber = new SubbedSubscriber();

        store.subscribe(subscriber);
        final Store.Subscription secondSubscription = store.subscribe(subscriber);

        secondSubscription.unsubscribe();

        store.dispatch(unknownAction());
        assertThat(subscriber.nbOnStateChangedCall).isEqualTo(1);
    }

    @Test
    public void supportsRemovingASubscriptionWithinASubscription() {
        final Store<State> store = createStore(Reducers.TODOS, new State());
        final Store.Subscriber subscriberA = mock(Store.Subscriber.class);
        final Store.Subscriber subscriberB = mock(Store.Subscriber.class);
        final Store.Subscriber subscriberC = mock(Store.Subscriber.class);

        store.subscribe(subscriberA);
        final OneTimeProxySubscriber subscriber = new OneTimeProxySubscriber(subscriberB);
        final Store.Subscription subscriptionB = store.subscribe(subscriber);
        subscriber.setCurrentSubscription(subscriptionB);

        store.subscribe(subscriberC);
        store.dispatch(unknownAction());
        store.dispatch(unknownAction());

        verify(subscriberA, times(2)).onStateChanged();
        verify(subscriberB, times(1)).onStateChanged();
        verify(subscriberC, times(2)).onStateChanged();
    }

    private static class OneTimeProxySubscriber implements Store.Subscriber {
        private final Store.Subscriber subscriberB;
        private Store.Subscription subscriptionB;

        OneTimeProxySubscriber(Store.Subscriber subscriberB) {
            this.subscriberB = subscriberB;
        }

        void setCurrentSubscription(Store.Subscription subscriptionB) {
            this.subscriptionB = subscriptionB;
        }

        @Override
        public void onStateChanged() {
            subscriberB.onStateChanged();
            subscriptionB.unsubscribe();
        }
    }

    @Test
    public void delaysUnsubscribeUntilTheEndOfCurrentDispatch() {
        final Store<State> store = createStore(Reducers.TODOS, new State());

        final List<Store.Subscription> subscriptions = new ArrayList<>();

        final Store.Subscriber subscriber1 = mock(Store.Subscriber.class);
        final Store.Subscriber subscriber2 = mock(Store.Subscriber.class);
        final Store.Subscriber subscriber3 = mock(Store.Subscriber.class);

        subscriptions.add(store.subscribe(subscriber1));
        subscriptions.add(store.subscribe(new Store.Subscriber() {
            @Override
            public void onStateChanged() {
                subscriber2.onStateChanged();
                for (Store.Subscription subscription : subscriptions) {
                    subscription.unsubscribe();
                }
            }
        }));

        subscriptions.add(store.subscribe(subscriber3));

        store.dispatch(unknownAction());
        verify(subscriber1, times(1)).onStateChanged();
        verify(subscriber2, times(1)).onStateChanged();
        verify(subscriber3, times(1)).onStateChanged();

        store.dispatch(unknownAction());
        verify(subscriber1, times(1)).onStateChanged();
        verify(subscriber2, times(1)).onStateChanged();
        verify(subscriber3, times(1)).onStateChanged();
    }

    @Test
    public void delaysSubscribeUntilTheEndOfCurrentDispatch() {
        final Store<State> store = createStore(Reducers.TODOS, new State());

        final Store.Subscriber subscriber1 = mock(Store.Subscriber.class);
        final Store.Subscriber subscriber2 = mock(Store.Subscriber.class);
        final Store.Subscriber subscriber3 = mock(Store.Subscriber.class);

        store.subscribe(subscriber1);
        store.subscribe(new Store.Subscriber() {
                            boolean subscriber3Added = false;

                            @Override
                            public void onStateChanged() {
                                subscriber2.onStateChanged();
                                if (!subscriber3Added) {
                                    subscriber3Added = true;
                                    store.subscribe(subscriber3);
                                }
                            }
                        }
        );

        store.dispatch(unknownAction());
        verify(subscriber1, times(1)).onStateChanged();
        verify(subscriber2, times(1)).onStateChanged();
        verify(subscriber3, times(0)).onStateChanged();

        store.dispatch(unknownAction());
        verify(subscriber1, times(2)).onStateChanged();
        verify(subscriber2, times(2)).onStateChanged();
        verify(subscriber3, times(1)).onStateChanged();
    }


    @Test
    public void usesTheLastSnapshotOfSubscribersDuringNestedDispatch() {
        final Store<State> store = createStore(Reducers.TODOS, new State());

        final Store.Subscriber subscriber1 = mock(Store.Subscriber.class);
        final Store.Subscriber subscriber2 = mock(Store.Subscriber.class);
        final Store.Subscriber subscriber3 = mock(Store.Subscriber.class);
        final Store.Subscriber subscriber4 = mock(Store.Subscriber.class);

        class Subscriber implements Store.Subscriber {
            Store.Subscription subscription1 = Store.Subscription.EMPTY;
            Store.Subscription subscription4 = Store.Subscription.EMPTY;

            Subscriber() {
                subscription1 = store.subscribe(this);
                store.subscribe(subscriber2);
                store.subscribe(subscriber3);
            }

            @Override
            public void onStateChanged() {
                subscriber1.onStateChanged();
                verify(subscriber1, times(1)).onStateChanged();
                verify(subscriber2, times(0)).onStateChanged();
                verify(subscriber3, times(0)).onStateChanged();
                verify(subscriber4, times(0)).onStateChanged();

                subscription1.unsubscribe();
                subscription4 = store.subscribe(subscriber4);
                store.dispatch(unknownAction());

                verify(subscriber1, times(1)).onStateChanged();
                verify(subscriber2, times(1)).onStateChanged();
                verify(subscriber3, times(1)).onStateChanged();
                verify(subscriber4, times(1)).onStateChanged();
            }
        }

        final Subscriber subscriber = new Subscriber();

        store.dispatch(unknownAction());
        verify(subscriber1, times(1)).onStateChanged();
        verify(subscriber2, times(2)).onStateChanged();
        verify(subscriber3, times(2)).onStateChanged();
        verify(subscriber4, times(1)).onStateChanged();

        subscriber.subscription4.unsubscribe();
        store.dispatch(unknownAction());
        verify(subscriber1, times(1)).onStateChanged();
        verify(subscriber2, times(3)).onStateChanged();
        verify(subscriber3, times(3)).onStateChanged();
        verify(subscriber4, times(1)).onStateChanged();
    }

    @Test
    public void providesAnUptodateStateWhenASubscriberIsNotified() {
        final Store<State> store = createStore(Reducers.TODOS, new State());
        store.subscribe(new Store.Subscriber() {
            @Override
            public void onStateChanged() {
                assertThat(store.getState()).isEqualTo(new State(new Todo(1, "Hello")));
            }
        });
        store.dispatch(addTodo("Hello"));
    }

    @Test
    public void handlesNestedDispatchesGracefully() {
        final Reducer<Integer> foo = new Reducer<Integer>() {
            @Override
            public Integer reduce(Integer state, Object action) {
                if ("foo".equals(action)) {
                    return 1;
                } else {
                    return state;
                }
            }
        };

        final Reducer<Integer> bar = new Reducer<Integer>() {
            @Override
            public Integer reduce(Integer state, Object action) {
                if ("bar".equals(action)) {
                    return 2;
                } else {
                    return state;
                }
            }
        };

        class CombinedStates {
            final int foo;
            final int bar;

            CombinedStates(int foo, int bar) {
                this.foo = foo;
                this.bar = bar;
            }
        }

        final Reducer<CombinedStates> combineReducers = new Reducer<CombinedStates>() {
            @Override
            public CombinedStates reduce(CombinedStates state, Object action) {
                return new CombinedStates(
                        foo.reduce(state.foo, action),
                        bar.reduce(state.bar, action)
                );
            }
        };

        final Store<CombinedStates> store = createStore(combineReducers, new CombinedStates(0, 0));

        final Store.Subscriber subscriberDispatching = new Store.Subscriber() {
            @Override
            public void onStateChanged() {
                CombinedStates state = store.getState();
                if (state.bar == 0) {
                    store.dispatch("bar");
                }
            }
        };

        store.subscribe(subscriberDispatching);
        store.dispatch("foo");

        assertThat(store.getState().foo).isEqualTo(1);
        assertThat(store.getState().bar).isEqualTo(2);
    }

    @Test(expected = Throwable.class)
    public void doesNotAllowDispatchFromWithinAReducer() {
        class DispatchInMiddle {
            final Store<Integer> store;

            DispatchInMiddle(Store<Integer> store) {
                this.store = store;
            }

            void dispatch() {
                store.dispatch("DispatchInMiddle");
            }
        }

        final Store<Integer> store = createStore(new Reducer<Integer>() {
            @Override
            public Integer reduce(Integer state, Object action) {
                if (action instanceof DispatchInMiddle) {
                    ((DispatchInMiddle) action).dispatch();
                }
                return state;
            }
        }, 0);

        store.dispatch(new DispatchInMiddle(store));
    }

    @Test
    public void doesNotThrowIfActionTypeIsUnknown() {
        final Store<State> store = createStore(Reducers.TODOS, new State());

        assertThat(store.dispatch("unknown")).isEqualTo("unknown");
    }

    private static class StubbedStateReducer implements Reducer<State> {
        private Object receivedAction;

        @Override
        public State reduce(State state, Object action) {
            receivedAction = action;
            return state;
        }
    }

    private class SubbedSubscriber implements Store.Subscriber {
        private int nbOnStateChangedCall = 0;

        SubbedSubscriber() {
        }

        @Override
        public void onStateChanged() {
            nbOnStateChangedCall++;
        }
    }
}
