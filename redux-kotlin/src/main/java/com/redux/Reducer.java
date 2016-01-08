package com.redux;

// Remove when dev tools are converted to Kotlin
@Deprecated
public interface Reducer<A, S> {
    S call(A action, S state);
}
