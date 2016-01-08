package com.redux.devtools;

import java.util.Arrays;
import java.util.Locale;

public class DevToolAction<A> {
    public static final String PERFORM_ACTION = "PERFORM_ACTION";
    public static final String RESET = "RESET";
    public static final String COMMIT = "COMMIT";
    public static final String ROLLBACK = "ROLLBACK";
    public static final String JUMP_TO_STATE = "JUMP_TO_STATE";
    public static final String ENABLE = "ENABLE";

    final String devToolActionType;
    private A applicationAction;
    private int index;
    private boolean isEnabled;

    private DevToolAction(String devToolActionType) {
        this.devToolActionType = devToolActionType;
    }

    public static <A> DevToolAction<A> forPerformAction(A action) {
        final DevToolAction<A> devToolAction = new DevToolAction<>(PERFORM_ACTION);
        devToolAction.applicationAction = action;
        return devToolAction;
    }

    public static <A> DevToolAction<A> forJumToState(int index) {
        final DevToolAction<A> devToolAction = new DevToolAction<>(JUMP_TO_STATE);
        devToolAction.index = index;
        return devToolAction;
    }

    public static <A> DevToolAction<A> forReset() {
        return new DevToolAction<>(RESET);
    }

    public static <A> DevToolAction<A> forCommit() {
        return new DevToolAction<>(COMMIT);
    }

    public static <A> DevToolAction<A> forRollback() {
        return new DevToolAction<>(ROLLBACK);
    }

    public static <A> DevToolAction<A> forEnable(int position, boolean enabled) {
        final DevToolAction<A> enabledAction = new DevToolAction<>(ENABLE);
        enabledAction.index = position;
        enabledAction.isEnabled = enabled;
        return enabledAction;

    }

    public A getApplicationAction() {
        assertDevToolActionTypeIs(PERFORM_ACTION);
        return applicationAction;
    }

    public int getIndex() {
        assertDevToolActionTypeIs(JUMP_TO_STATE, ENABLE);
        return index;
    }

    public boolean isEnabled() {
        assertDevToolActionTypeIs(ENABLE);
        return isEnabled;
    }

    private void assertDevToolActionTypeIs(String... actions) {
        Preconditions.checkState(
                Arrays.asList(actions).contains(devToolActionType),
                String.format(Locale.US, "The dev tool action should be in %s but was %s", Arrays.toString(actions), devToolActionType)
        );
    }

}
