package com.redux

fun checkState(condition: Boolean, messqge: String) {
    if (condition.not()) {
        throw IllegalStateException(messqge)
    }
}