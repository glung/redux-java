package com.glung.redux;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class MyReducerTest {

    private MyReducer reducer = new MyReducer();

    @Test
    public void ignore_unknown_actions() {
        assertThat(reducer.reduce(0, "unknown action"), is(0));
    }

    @Test
    public void increment() {
        assertThat(reducer.reduce(0, Action.INCREMENT), is(1));
    }

    @Test
    public void decrement() {
        assertThat(reducer.reduce(0, Action.DECREMENT), is(-1));
    }
}
