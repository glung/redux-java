package com.redux;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(library = true)
public class ReduxModule {

    public ReduxModule(Context context) {
        
    }

    @Provides @Singleton public Store<ActionCreator.Action, TodoState> provideStore(TodoReducer reducer) {
        return Store.create(TodoState.create(), reducer);
    }
}
