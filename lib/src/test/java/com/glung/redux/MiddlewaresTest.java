package com.glung.redux;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import redux.api.*;
import redux.api.Store;
import redux.api.enhancer.Middleware;
import redux.api.helpers.Reducers;
import redux.api.helpers.State;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static redux.api.helpers.ActionsCreator.addTodo;

public class MiddlewaresTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private redux.api.Store.Creator<State> storeCreator;
    @Mock
    private Store<State> storeMock;

    private final List<String> callOrderResult = new ArrayList<>();

    @Before
    public void init(){
        when(storeCreator.create(any(Reducer.class), any(State.class))).thenReturn(storeMock);
    }
    private Store<State> createStoreWithAppliedMiddlewares() {
        Store.Enhancer<State> middlewareEnhancer = Middlewares. applyMiddlewares(createMiddleware("ONE"),
                createMiddleware("TWO"), createMiddleware("THREE"));
        Store.Creator<State> creatorWithMiddlewares = middlewareEnhancer.enhance(storeCreator);
        return creatorWithMiddlewares.create(Reducers.TODOS, new State());
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
    public void dispatchInvokesMiddlewaresInCorrectOrder(){
        Store<State> store = createStoreWithAppliedMiddlewares();

        store.dispatch(addTodo("Test"));

        assertThat(callOrderResult).containsExactly("ONE", "TWO", "THREE");
    }

    @Test
    public void dispatchForwardsToTheOriginalStore(){
        Object action = addTodo("Test");
        Object expectedDispatchResult = new Object();
        when(storeMock.dispatch(action)).thenReturn(expectedDispatchResult);
        Store<State> store = createStoreWithAppliedMiddlewares();
        verifyZeroInteractions(storeMock);

        Object dispatchResult = store.dispatch(action);

        assertThat(dispatchResult).isEqualTo(expectedDispatchResult);
    }

    @Test
    public void getStateForwardsToTheOriginalStore(){
        Store<State> store = createStoreWithAppliedMiddlewares();
        verifyZeroInteractions(storeMock);

        store.getState();

        verify(storeMock).getState();
    }

    @Test
    public void replaceReducerForwardsToTheOriginalStore(){
        Store<State> store = createStoreWithAppliedMiddlewares();
        verifyZeroInteractions(storeMock);

        store.replaceReducer(Reducers.TODOS_REVERSE);

        verify(storeMock).replaceReducer(Reducers.TODOS_REVERSE);
    }

    @Test
    public void subscribeForwardsToTheOriginalStore(){
        Store.Subscriber subscriber = mock(Store.Subscriber.class);
        Store.Subscription expectedSubscription = mock(Store.Subscription.class);
        when(storeMock.subscribe(subscriber)).thenReturn(expectedSubscription);
        Store<State> store = createStoreWithAppliedMiddlewares();
        verifyZeroInteractions(storeMock);

        Store.Subscription subscription = store.subscribe(subscriber);

        assertThat(subscription).isEqualTo(expectedSubscription);
    }


}