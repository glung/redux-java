package com.redux;

import com.google.common.base.MoreObjects;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

public class ActionCreator {
    private final Store<Action, AppState> store;
    private final Operations operations;

    @Inject
    public ActionCreator(Store<Action, AppState> store, Operations operations) {
        this.store = store;
        this.operations = operations;
    }

    public rx.Subscription fetch() {
        store.dispatch(Action.forFetching(true));
        return operations
                .fetch()
                .subscribe(new Subscriber<List<Todo>>() {
                    @Override public void onCompleted() {
                        store.dispatch(Action.forFetching(false));
                    }

                    @Override public void onError(Throwable e) {
                        store.dispatch(Action.forFetching(false));
                    }

                    @Override public void onNext(List<Todo> todos) {
                        // TODO : in a real wworld situation dispatch all at once.
                        for (Todo todo : todos) {
                            store.dispatch(Action.forAdd(todo.text, todo.isCompleted));
                        }
                    }
                });
    }

    public void add(String text) {
        store.dispatch(Action.forAdd(text, false));
    }

    public void add(String text, boolean isCompleted) {
        store.dispatch(Action.forAdd(text, isCompleted));
    }

    public void delete(int id) {
        store.dispatch(Action.forDelete(id));
    }

    public void complete(int id, boolean isCompleted) {
        store.dispatch(Action.forComplete(id, isCompleted));
    }

    public void completeAll(boolean isCompleted) {
        store.dispatch(Action.forCompleteAll(isCompleted));
    }

    public void clearCompleted() {
        store.dispatch(Action.forClearCompleted());
    }

    public static class Action implements com.redux.Action {
        static final String KIND_INIT = "KIND_INIT";
        static final String KIND_ADD = "KIND_ADD";
        static final String KIND_DELETE = "KIND_DELETE";
        static final String KIND_COMPLETE = "KIND_COMPLETE";
        static final String KIND_COMPLETE_ALL = "KIND_COMPLETE_ALL";
        static final String KIND_CLEAR_ALL_COMPLETED = "KIND_CLEAR_ALL_COMPLETED";
        static final String KIND_FETCHING = "KIND_FETCHING";

        public static final String TEXT = "TEXT";
        public static final String ID = "ID";
        public static final String IS_COMPLETED = "IS_COMPLETED";
        public static final String IS_FETCHING = "IS_FETCHING";

        public final String type;
        public final Bundle bundle;

        private Action(String type) {
            this.type = type;
            bundle = new Bundle();
        }

        static Action forInit() {
            return new Action(KIND_INIT);
        }

        static Action forAdd(String text, boolean isCompleted) {
            final Action action = new Action(KIND_ADD);
            action.bundle
                    .put(TEXT, text)
                    .put(IS_COMPLETED, isCompleted);
            return action;
        }

        static Action forDelete(int id) {
            final Action action = new Action(KIND_DELETE);
            action.bundle.put(ID, id);
            return action;
        }

        static Action forComplete(int id, boolean isCompleted) {
            final Action action = new Action(KIND_COMPLETE);
            action.bundle
                    .put(ID, id)
                    .put(IS_COMPLETED, isCompleted);
            return action;
        }

        static Action forCompleteAll(boolean isCompleted) {
            final Action action = new Action(KIND_COMPLETE_ALL);
            action.bundle.put(IS_COMPLETED, isCompleted);
            return action;
        }

        static Action forClearCompleted() {
            return new Action(KIND_CLEAR_ALL_COMPLETED);
        }

        public static Action forFetching(boolean isFetching) {
            final Action action = new Action(KIND_FETCHING);
            action.bundle.put(IS_FETCHING, isFetching);
            return action;
        }

        @Override public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("type", type)
                    .add("bundle", bundle)
                    .toString();
        }
    }
}
