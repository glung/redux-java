package com.redux;

import rx.Subscriber;

import javax.inject.Inject;
import java.util.List;

public class ActionCreator {
    private final Store<AppAction, AppState> store;
    private final Operations operations;

    @Inject
    public ActionCreator(Store<AppAction, AppState> store, Operations operations) {
        this.store = store;
        this.operations = operations;
    }

    public rx.Subscription fetch() {
        store.dispatch(new AppAction.Fetching(true));
        return operations
                .fetch()
                .subscribe(new Subscriber<List<Todo>>() {
                    @Override public void onCompleted() {
                        store.dispatch(new AppAction.Fetching(false));
                    }

                    @Override public void onError(Throwable e) {
                        store.dispatch(new AppAction.Fetching(false));
                    }

                    @Override public void onNext(List<Todo> todos) {
                        // TODO : in a real wworld situation dispatch all at once.
                        for (Todo todo : todos) {
                            store.dispatch(new AppAction.Add(todo.getText(), todo.getIsCompleted()));
                        }
                    }
                });
    }

    public void add(String text) {
        store.dispatch(new AppAction.Add(text, false));
    }

    public void add(String text, boolean isCompleted) {
        store.dispatch(new AppAction.Add(text, isCompleted));
    }

    public void delete(int id) {
        store.dispatch(new AppAction.Delete(id));
    }

    public void complete(int id, boolean isCompleted) {
        store.dispatch(new AppAction.Complete(id, isCompleted));
    }

    public void completeAll(boolean isCompleted) {
        store.dispatch(new AppAction.CompleteAll(isCompleted));
    }

    public void clearCompleted() {
        store.dispatch(new AppAction.ClearCompleted());
    }

}
