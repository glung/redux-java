package com.redux;

// f(Action, State) -> State
public interface Reducer<A extends Action, S extends State> {
    S call(A action, S state);
}
