package com.android;

import com.redux.ReduxModule;

import dagger.ObjectGraph;

public class Application extends android.app.Application {

    private static Application instance;
    private ObjectGraph objectGraph;

    @Override
    public void onCreate() {
		super.onCreate();

        instance = this;
        objectGraph = ObjectGraph.create(new ApplicationModule(), new ReduxModule());
    }

    public static ObjectGraph getObjectGraph() {
        return instance.objectGraph;
    }
}
