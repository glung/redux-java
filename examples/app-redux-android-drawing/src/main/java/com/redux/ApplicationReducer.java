package com.redux;

import com.redux.storage.StateTree;
import com.redux.draw.DrawingReducer;

import java.util.Collections;

import javax.inject.Inject;

public class ApplicationReducer extends CombinedReducers<Redux.MyAction, StateTree> implements Redux.MyReducer {

    @Inject
    public ApplicationReducer(DrawingReducer drawingReducer) {
        super(Collections.singletonList(drawingReducer));
    }
}
