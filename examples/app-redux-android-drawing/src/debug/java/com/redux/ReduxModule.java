package com.redux;

import com.redux.Redux.MyAction;
import com.redux.storage.StateTree;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(library = true)
public class ReduxModule {

    @Provides @Singleton public DevTool<MyAction, StateTree> provideDevTools(ApplicationReducer reducer) {
        return new DevTool.Builder<>(MyAction.INIT, StateTree.create(), reducer).create();
    }

    @Provides public Store<DevToolAction<MyAction>, DevToolState<MyAction, StateTree>> provideDevToolsStore(DevTool<MyAction, StateTree> devTool) {
        return devTool.getDevToolsStore();
    }

    @Provides public Store<MyAction, StateTree> provideApplicationStore(DevTool<MyAction, StateTree> devTool) {
        return devTool.getApplicationStore();
    }


    @Provides public DevToolPresenter<MyAction, StateTree> provideController(Store<DevToolAction<MyAction>, DevToolState<MyAction, StateTree>> liftedStore) {
        return new DevToolPresenter<>(liftedStore);
    }

}
