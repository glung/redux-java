package com.redux.draw.reducer;

import com.redux.Redux;
import com.redux.draw.DrawingActions;
import com.redux.draw.interpolation.NatCubic;
import com.redux.draw.interpolation.NatCubicContext;
import com.redux.draw.model.MyPath;
import com.redux.storage.StateTree;

import javax.inject.Inject;

public class CubicSplineInterpolationReducer implements Redux.MyReducer {

    final private NatCubicContext context;

    @Inject
    public CubicSplineInterpolationReducer() {
        context = new NatCubicContext(1 / 15f);
    }


    @Override
    public StateTree call(Redux.MyAction action, StateTree state) {
        switch (action.type) {
            case DrawingActions.InterpolatePath.ACTION:
                return replaceLastLine(state);
            default:
                return state;
        }
    }

    private StateTree replaceLastLine(StateTree state) {
        final MyPath interpolatedPath = NatCubic.process(context, state.getLast());
        return state.removeLast().addLast(interpolatedPath);
    }

}
