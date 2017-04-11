package com.glung.redux;

import static com.glung.redux.Middlewares.applyMiddlewares;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static redux.api.helpers.ActionsCreator.addTodo;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import redux.api.Dispatcher;
import redux.api.Reducer;
import redux.api.Store;
import redux.api.enhancer.Middleware;
import redux.api.helpers.Reducers;
import redux.api.helpers.State;

import java.util.ArrayList;
import java.util.List;

public class MiddlewaresTest {

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock private redux.api.Store.Creator<State> storeCreator;
    @Mock private Store<State> storeMock;

    private final List<String> callOrderResult = new ArrayList<>();

    private Store.Creator<State> enhancedStoreCreator() {
        when(storeCreator.create(any(Reducer.class), any(State.class))).thenReturn(storeMock);

        return applyMiddlewares(createMiddleware("ONE"),
                                createMiddleware("TWO"),
                                createMiddleware("THREE"))
                .enhance(storeCreator);
    }

    private Middleware<State> createMiddleware(final String identifier) {
        return new Middleware<State>() {
            @Override
            public Object dispatch(Store<State> store, Dispatcher next, Object action) {
                callOrderResult.add(identifier);
                return next.dispatch(action);
            }
        };
    }

    @Test
    public void dispatchInvokesMiddlewaresInCorrectOrder() {
        final Store<State> store = enhancedStoreCreator().create(Reducers.TODOS, new State());

        store.dispatch(addTodo("Test"));

        assertThat(callOrderResult).containsExactly("ONE", "TWO", "THREE");
    }

    @Test
    public void dispatchForwardsToTheOriginalStore() {
        final Object action = addTodo("Test");
        final Object expectedDispatchResult = new Object();
        final Store<State> store = enhancedStoreCreator().create(Reducers.TODOS, new State());
        when(storeMock.dispatch(action)).thenReturn(expectedDispatchResult);

        verifyZeroInteractions(storeMock);

        Object dispatchResult = store.dispatch(action);

        assertThat(dispatchResult).isEqualTo(expectedDispatchResult);
    }

    @Test
    public void getStateForwardsToTheOriginalStore() {
        final Store<State> store = enhancedStoreCreator().create(Reducers.TODOS, new State());
        verifyZeroInteractions(storeMock);

        store.getState();

        verify(storeMock).getState();
    }

    @Test
    public void replaceReducerForwardsToTheOriginalStore() {
        final Store<State> store = enhancedStoreCreator().create(Reducers.TODOS, new State());
        verifyZeroInteractions(storeMock);

        store.replaceReducer(Reducers.TODOS_REVERSE);

        verify(storeMock).replaceReducer(Reducers.TODOS_REVERSE);
    }

    @Test
    public void subscribeForwardsToTheOriginalStore() {
        final Store.Subscriber subscriber = mock(Store.Subscriber.class);
        final Store.Subscription expectedSubscription = mock(Store.Subscription.class);
        final Store<State> store = enhancedStoreCreator().create(Reducers.TODOS, new State());
        when(storeMock.subscribe(subscriber)).thenReturn(expectedSubscription);

        verifyZeroInteractions(storeMock);

        Store.Subscription subscription = store.subscribe(subscriber);

        assertThat(subscription).isEqualTo(expectedSubscription);
    }
}
