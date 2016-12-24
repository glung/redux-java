package com.redux;

// f(Action, State) -> State
// in kotlin , it's val reducer:(val action:Action , val state: State) -> action
public interface Reducer<A extends Action, S extends State> {
    S call(A action, S state);
}
