package com.redux;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Service {
    private static final int NOT_SET = -1;
    private static final String[] LIST_OF_TODO = {
            "Call my mum",
            "Clean my room",
            "Do my German homework",
            "Ask the permission for using the name redux-java",
            "Go to work",
            "Go to the supermarket, buy food and beers",
            "Port this todolist reducer to Kotlin",
            "Take a look at react native"
    };

    private Random random;

    @Inject
    public Service() {
        random = new Random();
    }

    public List<Todo> get() throws IOException, InterruptedException {
        // We simulate a long operation
        TimeUnit.SECONDS.sleep(random.nextInt(5));

        // Fill with fake data
        final ArrayList<Todo> todos = new ArrayList<>();
        for (int i = 0, size = random.nextInt(4); i < size; i++) {
            todos.add(createTodo(LIST_OF_TODO[random.nextInt(LIST_OF_TODO.length)], random.nextBoolean()));
        }

        return todos;
    }

    private static Todo createTodo(String text, boolean isCompleted) {
        return new Todo(NOT_SET, text, isCompleted);
    }
}
