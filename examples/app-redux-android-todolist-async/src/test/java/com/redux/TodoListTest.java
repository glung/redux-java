package com.redux;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class TodoListTest {

    private TodoList state;

    @Before
    public void setUp() {
        state = TodoList.create();
    }

    @Test
    public void createFromShouldOrderTodoByAscendantId() {
        final Todo todo1 = new Todo(1, "todo1", false);
        final Todo todo2 = new Todo(2, "todo2", false);
        final Todo todo0 = new Todo(0, "todo0", false);

        assertThat(TodoList.from(Arrays.asList(todo1, todo2, todo0))
                .get())
                .containsExactly(todo0, todo1, todo2);
    }

    @Test
    public void addShouldPreserveThePreviousVersion() {
        final Todo todo = new Todo(0, "test", false);

        assertThat(state.add(todo).get()).containsExactly(todo);
        assertThat(state.get()).isEmpty();
    }

    @Test
    public void removeShouldPreserveThePreviousVersion() {
        final Todo todo = new Todo(0, "test", false);
        state = TodoList.from(todo);

        assertThat(state.remove(todo).get()).isEmpty();
        assertThat(state.get()).containsExactly(todo);
    }

    @Test
    public void addTodoShouldBeOrderedByAscendantId() {
        final Todo todo1 = new Todo(1, "todo1", false);
        final Todo todo2 = new Todo(2, "todo2", false);
        final Todo todo0 = new Todo(0, "todo0", false);

        assertThat(state.add(todo1).add(todo2).add(todo0)
                .get())
                .containsExactly(todo0, todo1, todo2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findShouldThrowIllegalArgumentExceptionWhenNotFound() {
        state.find(333);
    }

    @Test
    public void findShouldReturnTodo() {
        final Todo todo = new Todo(5, "text", true);
        state = this.state.add(todo);

        assertThat(state.find(5)).isSameAs(todo);
    }

    @Test
    public void nextFreeIdShouldReturn0WhenEmpty() {
        assertThat(state.nextFreeId()).isEqualTo(0);
    }

    @Test
    public void nextFreeIdShouldReturnMaxIdPlus1() {
        state = state
                .add(new Todo(5, "text", true))
                .add(new Todo(8, "text", true))
                .add(new Todo(2, "text", true));

        assertThat(state.nextFreeId()).isEqualTo(9);
    }


    @Test(expected = UnsupportedOperationException.class)
    public void getTodoListShouldReturnUnmodifiableList() {
        state.get().add(new Todo(0, "test", true));
    }

}