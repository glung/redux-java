package com.redux;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.redux.devtools.DevTool;
import com.redux.devtools.DevToolAction;
import com.redux.devtools.DevToolPresenter;
import com.redux.devtools.DevToolState;
import com.redux.devtools.ApplicationStateMonitor;

import java.io.File;
import java.util.Collections;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(library = true)
public class ReduxModule {

    private final Context context;

    public ReduxModule(Context context) {
        this.context = context;
    }

    @Provides @Singleton public DevTool<AppAction, AppState> provideDevToolsBuilder() {
        final File file = new File(context.getFilesDir(), "redux_debug.json");
        final StateConverter statePrettyPrintConverter = new StateConverter(new GsonBuilder().setPrettyPrinting().create());

        final AppState appState = new AppState(Collections.<Todo>emptyList(), false);
        return new DevTool.Builder<>(AppAction.Init.INSTANCE, appState, com.redux.AppReducerKt.getReducer())
                .withMonitor(ApplicationStateMonitor.printStream(System.err, statePrettyPrintConverter))
                .withSessionPersistence(file, ActionConverter.INSTANCE, new StateConverter(new Gson()))
                .create();
    }

    @Provides public Store<DevToolAction<AppAction>, DevToolState<AppAction, AppState>> provideStore(DevTool<AppAction, AppState> devTool) {
        return devTool.getDevToolsStore();
    }

    @Provides public Store<AppAction, AppState> provideUnliftedStore(DevTool<AppAction, AppState> devTool) {
        return devTool.getApplicationStore();
    }

    @Provides public DevToolPresenter<AppAction, AppState> provideController(Store<DevToolAction<AppAction>, DevToolState<AppAction, AppState>> liftedStore) {
        return new DevToolPresenter<>(liftedStore);
    }

}
