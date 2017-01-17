package com.glung.redux;

import redux.api.Reducer;

public class StoreTest extends redux.api.StoreTest {

    @Override
    public <S> redux.api.Store createStore(Reducer<S> reducer, S state) {
        return new Store.Creator<S>().create(reducer, state);
    }
}
