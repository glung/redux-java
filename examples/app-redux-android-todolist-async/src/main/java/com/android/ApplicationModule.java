package com.android;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import com.redux.ReduxModule;

import dagger.Module;
import dagger.Provides;

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

    @Provides InputMethodManager provideInputMethodManager(Context context) {
        return (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }
}
