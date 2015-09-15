package com.redux;

import com.google.common.base.MoreObjects;
import com.redux.storage.StateTree;

public class Redux {

    public static class MyAction implements Action {

        public static final MyAction INIT = new MyAction("INIT");

        public final String type;

        public MyAction(String type) {
            this.type = type;
        }

        @Override public String toString() {
            return MoreObjects
                    .toStringHelper(this)
                    .add("type", type)
                    .toString();
        }
    }

    public interface MyReducer extends Reducer<MyAction, StateTree> {

    }

}
