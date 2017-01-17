package com.glung.redux;

class MyReducer implements redux.api.Reducer<Integer> {
    @Override
    public Integer reduce(Integer state, Object action) {
        return action instanceof Action ? reduce(state, (Action) action) : state;
    }

    private Integer reduce(Integer state, Action action) {
        switch (action) {
            case INCREMENT:
                return state + 1;
            case DECREMENT:
                return state - 1;
            default:
                return state;
        }
    }
}
