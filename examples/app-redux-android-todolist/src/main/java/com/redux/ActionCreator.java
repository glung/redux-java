package com.redux;

import com.google.common.base.MoreObjects;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

public class ActionCreator {
    private final Store<Action, TodoState> store;

    @Inject
    public ActionCreator(Store<Action, TodoState> store) {
        this.store = store;
    }

    public void init() {
        store.dispatch(Action.forInit());
    }

    public void add(String text) {
        store.dispatch(Action.fotAdd(text));
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
        public static final String INIT = "INIT";
        public static final String ADD = "ADD";
        public static final String DELETE = "DELETE";
        public static final String COMPLETE = "COMPLETE";
        public static final String COMPLETE_ALL = "COMPLETE_ALL";
        public static final String CLEAR_ALL_COMPLETED = "CLEAR_ALL_COMPLETED";

        private final String type;
        private final String text;
        private final Integer id;
        private final Boolean isCompleted;

        private Action(String type, String text, Integer id, Boolean isCompleted) {
            this.type = type;
            this.text = text;
            this.isCompleted = isCompleted;
            this.id = id;
        }

        static Action forInit() {
            return new Action(INIT, null, null, null);
        }

        static Action fotAdd(String text) {
            return new Action(ADD, text, null, null);
        }

        static Action forDelete(int id) {
            return new Action(DELETE, null, id, null);
        }

        static Action forComplete(int id, boolean isCompleted) {
            return new Action(COMPLETE, null, id, isCompleted);
        }

        static Action forCompleteAll(boolean isCompleted) {
            return new Action(COMPLETE_ALL, null, null, isCompleted);
        }

        static Action forClearCompleted() {
            return new Action(CLEAR_ALL_COMPLETED, null, null, null);
        }

        String getType() {
            return type;
        }

        int getId() {
            return checkNotNull(id, "Id is undefined for this action(" + type + ")");
        }

        boolean isCompleted() {
            return checkNotNull(isCompleted, "isCompleted is undefined for this action(" + type + ")");
        }

        String getText() {
            return checkNotNull(text, "text is undefined for this action(" + type + ")");
        }

        @Override public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("type", type)
                    .add("id", id)
                    .add("isCompleted", isCompleted)
                    .add("text", text)
                    .toString();
        }
    }
}
