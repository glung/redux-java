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
        final TodoState state = TodoState.create();

        assertThat(new TodoReducer()
                .call(ActionCreator.Action.fotAdd("test"), state)
                .getTodoList())
                .containsExactly(new Todo(state.nextFreeId(), "test", false));
    }

    @Test
    public void testDeleteTodoAction() {
        final Todo todo = new Todo(1, "test", true);
        final TodoState state = TodoState.create().add(todo);

        assertThat(new TodoReducer()
                .call(ActionCreator.Action.forDelete(todo.id), state)
                .getTodoList())
                .isEmpty();
    }

    @Test
    public void testCompleteTodoAction() {
        final Todo todo = new Todo(1, "test", false);
        final TodoState state = TodoState.create().add(todo);

        assertThat(new TodoReducer()
                .call(ActionCreator.Action.forComplete(todo.id, true), state)
                .getTodoList())
                .containsExactly(new Todo(1, "test", true));
    }

    @Test
    public void testUnCompleteTodoAction() {
        final Todo todo = new Todo(1, "test", true);
        final TodoState state = TodoState.create().add(todo);

        assertThat(new TodoReducer()
                .call(ActionCreator.Action.forComplete(todo.id, false), state)
                .getTodoList())
                .containsExactly(new Todo(1, "test", false));
    }

    @Test
    public void testCompleteAllShould() {
        final Todo todo1 = new Todo(1, "test1", false);
        final Todo todo2 = new Todo(2, "test2", true);
        final Todo todo3 = new Todo(3, "test3", false);
        final TodoState state = TodoState
                .create()
                .add(todo1)
                .add(todo2)
                .add(todo3);

        assertThat(new TodoReducer()
                .call(ActionCreator.Action.forCompleteAll(true), state)
                .getTodoList())
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
        final TodoState state = TodoState
                .create()
                .add(todo1)
                .add(todo2)
                .add(todo3);

        assertThat(new TodoReducer()
                .call(ActionCreator.Action.forCompleteAll(false), state)
                .getTodoList())
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
        final TodoState state = TodoState
                .create()
                .add(todo1)
                .add(todo2)
                .add(todo3);

        assertThat(new TodoReducer()
                .call(ActionCreator.Action.forClearCompleted(), state)
                .getTodoList())
                .containsExactly(
                        new Todo(1, "test1", false),
                        new Todo(3, "test3", false)
                );
    }

    @Test
    public void testUnknownActionShouldBeIgnored() {
        final TodoState state = TodoState.create();
        assertThat(new TodoReducer()
                .call(ActionCreator.Action.forInit(), state))
                .isSameAs(state);
    }

}