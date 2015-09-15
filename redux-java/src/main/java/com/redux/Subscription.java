package com.redux;

import java.util.List;

public class Subscription<A extends Action, S extends State> {
    private final List<Subscriber> subscribers;
    private final Subscriber subscriber;

    public Subscription(List<Subscriber> subscribers, Subscriber subscriber) {
        this.subscribers = subscribers;
        this.subscriber = subscriber;
    }

    public void unsubscribe() {
        subscribers.remove(subscriber);
    }
}
