package com.redux;

import android.content.Context;

import java.util.Collections;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(library = true)
public class ReduxModule {

    public ReduxModule(Context context) {
        
    }

    @Provides @Singleton public Store<AppAction, AppState> provideStore() {
        return Store.create(new AppState(Collections.<Todo>emptyList(), false), com.redux.AppReducerKt.getReducer());
    }
}
