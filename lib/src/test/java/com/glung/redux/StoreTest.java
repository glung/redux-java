package com.glung.redux;

import org.jetbrains.annotations.NotNull;
import redux.api.Reducer;

public class StoreTest extends redux.api.StoreTest {

    @NotNull
    @Override
    public <S> redux.api.Store<S> createStore(@NotNull Reducer<S> reducer, @NotNull S state) {
        return new Store.Creator().create(reducer, state);
    }
}
