package com.redux

import com.redux
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

public class TodoReducerTest {

    @Test fun testAddTodoAction() {
        assertThat(
                redux.reducer.call(AppAction.Add("test", false), AppState()).list
        ).containsExactly(Todo(0, "test", false))
    }

    @Test fun testDeleteTodoAction() {
        val todo = Todo(1, "test", true)
        val state = AppState(listOf(todo))

        assertThat(redux.reducer.call(AppAction.Delete(todo.id), state).list).isEmpty()
    }

    @Test fun testCompleteTodoAction() {
        val todo = Todo(1, "test", false)
        val state = AppState(listOf(todo))

        assertThat(
                redux.reducer.call(AppAction.Complete(todo.id, true), state).list
        ).containsExactly(Todo(1, "test", true))
    }

    @Test fun testUnCompleteTodoAction() {
        val todo = Todo(1, "test", true)
        val state = AppState(listOf(todo))

        assertThat(
                redux.reducer.call(AppAction.Complete(todo.id, false), state).list
        ).containsExactly(Todo(1, "test", false))
    }

    @Test fun testCompleteAllShould() {
        val todo1 = Todo(1, "test1", false)
        val todo2 = Todo(2, "test2", true)
        val todo3 = Todo(3, "test3", false)
        val state = AppState(listOf(todo1, todo2, todo3))

        assertThat(
                redux.reducer.call(AppAction.CompleteAll(true), state).list
        ).containsExactly(
                Todo(1, "test1", true),
                Todo(2, "test2", true),
                Todo(3, "test3", true)
        )
    }


    @Test fun testUnCompleteAllShould() {
        val todo1 = Todo(1, "test1", false)
        val todo2 = Todo(2, "test2", true)
        val todo3 = Todo(3, "test3", false)
        val state = AppState(listOf(todo1, todo2, todo3))

        assertThat(
                redux.reducer.call(AppAction.CompleteAll(false), state).list
        ).containsExactly(
                Todo(1, "test1", false),
                Todo(2, "test2", false),
                Todo(3, "test3", false)
        )
    }

    @Test fun testClearCompleted() {
        val todo1 = Todo(1, "test1", false)
        val todo2 = Todo(2, "test2", true)
        val todo3 = Todo(3, "test3", false)
        val state = AppState(listOf(todo1, todo2, todo3))

        assertThat(
                redux.reducer.call(AppAction.ClearCompleted, state).list
        ).containsExactly(
                Todo(1, "test1", false),
                Todo(3, "test3", false)
        )
    }

}