package com.redux;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(library = true)
public class ReduxModule {

    private final Context context;

    public ReduxModule(Context context) {
        this.context = context;
    }

    @Provides @Singleton public DevTool<ActionCreator.Action, AppState> provideDevToolsBuilder(TodoReducer reducer) {
        final File file = new File(context.getFilesDir(), "redux_debug.json");
        final StateConverter statePrettyPrintConverter = new StateConverter(new GsonBuilder().setPrettyPrinting().create());
        
        return new DevTool.Builder<>(ActionCreator.Action.forInit(), AppState.create(), reducer)
                .withMonitor(ApplicationStateMonitor.printStream(System.err, statePrettyPrintConverter))
                .withSessionPersistence(file, new ActionConverter(new Gson()), new StateConverter(new Gson()))
                .create();
    }

    @Provides public Store<DevToolAction<ActionCreator.Action>, DevToolState<ActionCreator.Action, AppState>> provideStore(DevTool<ActionCreator.Action, AppState> devTool) {
        return devTool.getDevToolsStore();
    }

    @Provides public Store<ActionCreator.Action, AppState> provideUnliftedStore(DevTool<ActionCreator.Action, AppState> devTool) {
        return devTool.getApplicationStore();
    }

    @Provides public DevToolPresenter<ActionCreator.Action, AppState> provideController(Store<DevToolAction<ActionCreator.Action>, DevToolState<ActionCreator.Action, AppState>> liftedStore) {
        return new DevToolPresenter<>(liftedStore);
    }

}
