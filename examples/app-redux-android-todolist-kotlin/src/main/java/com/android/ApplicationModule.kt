package com.android

import android.content.Context

import com.redux.Store
import com.redux.TodoListAction
import com.redux.TodoListState

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

@Module(injects = TodoActivity::class)
class ApplicationModule(private val application: Context) {

    @Provides internal fun provideApplicationContext(): Context {
        return application
    }

    @Singleton @Provides internal fun provideStore(): Store<TodoListAction, TodoListState> {
        return configureStore(TodoListState())
    }
}
