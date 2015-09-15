package com.redux;

import com.redux.storage.StateTree;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(library = true)
public class ReduxModule {

    @Provides @Singleton public Store<Redux.MyAction, StateTree> provideStore(ApplicationReducer reducer) {
        return Store.create(StateTree.create(), reducer);
    }
}
