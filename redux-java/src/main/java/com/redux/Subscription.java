package com.redux;

import java.util.List;

public abstract class Subscription {
    private static final Subscription EMPTY = new Subscription() {
        @Override public void unsubscribe() {}
    };

    public abstract void unsubscribe();

    public static Subscription create(final List<Subscriber> subscribers, final Subscriber subscriber) {
        return new Subscription() {
            @Override public void unsubscribe() {
                subscribers.remove(subscriber);
            }
        };
    }

    public static Subscription empty() {
        return EMPTY;
    }
}
