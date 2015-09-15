package com.redux;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TodoReducerTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testAddTodoAction() {
        final AppState state = AppState.create();

        assertThat(new TodoReducer()
                .call(ActionCreator.Action.forAdd("test", false), state)
                .todoList.get())
                .containsExactly(new Todo(state.todoList.nextFreeId(), "test", false));
    }

    @Test
    public void testDeleteTodoAction() {
        final Todo todo = new Todo(1, "test", true);
        final AppState state = AppState.create(TodoList.create().add(todo));

        assertThat(new TodoReducer()
                .call(ActionCreator.Action.forDelete(todo.id), state)
                .todoList.get())
                .isEmpty();
    }

    @Test
    public void testCompleteTodoAction() {
        final Todo todo = new Todo(1, "test", false);
        final AppState state = AppState.create(TodoList.create().add(todo));

        assertThat(new TodoReducer()
                .call(ActionCreator.Action.forComplete(todo.id, true), state)
                .todoList.get())
                .containsExactly(new Todo(1, "test", true));
    }

    @Test
    public void testUnCompleteTodoAction() {
        final Todo todo = new Todo(1, "test", true);
        final AppState state = AppState.create(TodoList.create().add(todo));

        assertThat(new TodoReducer()
                .call(ActionCreator.Action.forComplete(todo.id, false), state)
                .todoList.get())
                .containsExactly(new Todo(1, "test", false));
    }

    @Test
    public void testCompleteAllShould() {
        final Todo todo1 = new Todo(1, "test1", false);
        final Todo todo2 = new Todo(2, "test2", true);
        final Todo todo3 = new Todo(3, "test3", false);
        final AppState state = AppState.create(TodoList
                .create()
                .add(todo1)
                .add(todo2)
                .add(todo3));

        assertThat(new TodoReducer()
                .call(ActionCreator.Action.forCompleteAll(true), state)
                .todoList.get())
                .containsExactly(
                        new Todo(1, "test1", true),
                        new Todo(2, "test2", true),
                        new Todo(3, "test3", true)
                );
    }


    @Test
    public void testUnCompleteAllShould() {
        final Todo todo1 = new Todo(1, "test1", false);
        final Todo todo2 = new Todo(2, "test2", true);
        final Todo todo3 = new Todo(3, "test3", false);
        final AppState state = AppState.create(TodoList
                .create()
                .add(todo1)
                .add(todo2)
                .add(todo3));

        assertThat(new TodoReducer()
                .call(ActionCreator.Action.forCompleteAll(false), state)
                .todoList.get())
                .containsExactly(
                        new Todo(1, "test1", false),
                        new Todo(2, "test2", false),
                        new Todo(3, "test3", false)
                );
    }

    @Test
    public void testClearCompleted() {
        final Todo todo1 = new Todo(1, "test1", false);
        final Todo todo2 = new Todo(2, "test2", true);
        final Todo todo3 = new Todo(3, "test3", false);
        final AppState state = AppState.create(TodoList
                .create()
                .add(todo1)
                .add(todo2)
                .add(todo3));

        assertThat(new TodoReducer()
                .call(ActionCreator.Action.forClearCompleted(), state)
                .todoList.get())
                .containsExactly(
                        new Todo(1, "test1", false),
                        new Todo(3, "test3", false)
                );
    }

}