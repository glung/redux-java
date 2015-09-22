package com.redux;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

public class Operations {

    private final Service service;

    @Inject
    public Operations(Service service) {
        this.service = service;
    }

    Observable<List<Todo>> fetch() {
        return Observable
                .create(new Observable.OnSubscribe<List<Todo>>() {
                    @Override public void call(Subscriber<? super List<Todo>> subscriber) {
                        try {
                            subscriber.onNext(service.get());
                        } catch (IOException | InterruptedException e) {
                            subscriber.onError(e);
                        }
                        subscriber.onCompleted();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
