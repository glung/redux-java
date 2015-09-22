package com.redux;

import dagger.Module;
import dagger.Provides;

import android.content.Context;

import javax.inject.Singleton;
import java.util.Collections;

@Module(library = true)
public class ReduxModule {

    public ReduxModule(Context context) {
        
    }

    @Provides @Singleton public Store<AppAction, AppState> provideStore() {
        return Store.create(new AppState(Collections.<Todo>emptyList(), false), new TodoReducer());
    }
}
