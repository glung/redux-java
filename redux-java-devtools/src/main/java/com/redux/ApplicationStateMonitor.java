package com.redux;

import java.io.PrintStream;

public abstract class ApplicationStateMonitor<S extends State> {
    public abstract void print(S state);

    public static <S extends State> ApplicationStateMonitor<S> printStream(final PrintStream stream, final Converter<S> stateConverter) {
        return new ApplicationStateMonitor<S>() {
            @Override public void print(S state) {
                stream.println(stateConverter.toJson(state));
            }
        };
    }

}
