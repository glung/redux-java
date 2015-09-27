package com.redux

import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

import javax.inject.Inject
import java.io.IOException

public class Operations @Inject constructor(private val service: Service) {

    fun fetch(): Observable<List<Todo>> {
        return Observable.create(object : Observable.OnSubscribe<List<Todo>> {
            override fun call(subscriber: Subscriber<in List<Todo>>) {
                try {
                    subscriber.onNext(service.get())
                } catch (e: IOException) {
                    subscriber.onError(e)
                } catch (e: InterruptedException) {
                    subscriber.onError(e)
                }

                subscriber.onCompleted()
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
}
