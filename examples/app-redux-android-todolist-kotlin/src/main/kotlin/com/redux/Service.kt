package com.redux

import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

public class Service @Inject constructor() {
    companion object {
        private val random: Random = Random()
        private val NOT_SET = -1
        private val LIST_OF_TODO = arrayOf("Call my mum", "Clean my room", "Do my German homework", "Ask the permission for using the name redux-java", "Go to work", "Go to the supermarket, buy food and beers", "Port this todolist reducer to Kotlin", "Take a look at react native")
    }

    public fun get(): List<Todo> {
        // We simulate a long operation
        TimeUnit.SECONDS.sleep(random.nextInt(5).toLong())

        // Fill with fake data
        val size = random.nextInt(4)
        val todos = ArrayList<Todo>(size)
        repeat(size) {
            todos.add(Todo(NOT_SET, LIST_OF_TODO[random.nextInt(LIST_OF_TODO.size())], random.nextBoolean()))
        }

        return todos
    }
}
