package com.android;

import com.redux.ReduxModule;
import dagger.Module;
import dagger.Provides;

import android.content.Context;

@Module(
        includes = ReduxModule.class,
        injects = TodoActivity.class
)
public class ApplicationModule {

    private final Context application;

    public ApplicationModule(Context application) {
        this.application = application;
    }

    @Provides Context provideApplicationContext() {
        return application;
    }
}
