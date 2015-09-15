package com.redux.draw;

import com.redux.Redux;
import com.redux.storage.StateTree;
import com.redux.draw.reducer.CubicSplineInterpolationReducer;
import com.redux.draw.reducer.LinearInterpolationReducer;
import com.redux.CombinedReducers;

import java.util.Arrays;

import javax.inject.Inject;

public class DrawingReducer extends CombinedReducers<Redux.MyAction, StateTree> implements Redux.MyReducer {

    @Inject
    public DrawingReducer(
            LinearInterpolationReducer linearInterpolationReducer,
            CubicSplineInterpolationReducer cubicSplineInterpolationReducer) {
        super(Arrays.asList(linearInterpolationReducer, cubicSplineInterpolationReducer));
    }
}
