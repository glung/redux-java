package com.redux;

import java.util.Arrays;
import java.util.List;

public class CombinedReducers<A extends Action, S extends State> implements Reducer<A, S> {

    @SafeVarargs public static <A extends Action, S extends State> CombinedReducers<A, S> from(Reducer<A, S>... reducers) {
        return new CombinedReducers<>(Arrays.asList(reducers));
    }

    private final List<? extends Reducer<A, S>> reducers;

    public CombinedReducers(List<? extends Reducer<A, S>> reducers) {
        this.reducers = reducers;
    }

    @Override
    public S call(A action, S state) {
        for (Reducer<A, S> reducer : reducers) {
            state = reducer.call(action, state);
        }
        return state;
    }

}
