package com.android;

import android.content.Context;

import com.redux.Store;
import com.redux.TodoListAction;
import com.redux.TodoListState;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(injects = TodoActivity.class)
public class ApplicationModule {

    private final Context application;

    public ApplicationModule(Context application) {
        this.application = application;
    }

    @Provides Context provideApplicationContext() {
        return application;
    }

    @Singleton @Provides Store<TodoListAction, TodoListState> provideStore() {
        return ConfigureStoreKt.configureStore(new TodoListState());
    }
}
