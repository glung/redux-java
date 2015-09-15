package com.redux.draw.reducer;

import com.redux.Redux;
import com.redux.draw.DrawingActions.AddPointsToPath;
import com.redux.draw.DrawingActions.NewPath;
import com.redux.draw.model.MyPath;
import com.redux.storage.StateTree;

import javax.inject.Inject;

public class LinearInterpolationReducer implements Redux.MyReducer {

    @Inject
    public LinearInterpolationReducer() {
    }

    @Override
    public StateTree call(Redux.MyAction action, StateTree state) {
        switch (action.type) {
            case NewPath.ACTION:
                return createNewLine(state);
            case AddPointsToPath.ACTION:
                final AddPointsToPath addPoints = (AddPointsToPath) action;
                return addPoints(state, addPoints.xAxes, addPoints.yAxes);
            default:
                return state;
        }
    }

    private StateTree createNewLine(StateTree state) {
        return state.addLast(MyPath.create());
    }

    private StateTree addPoints(StateTree state, float[] x, float[] y) {
        final MyPath updatedPath = state.getLast().append(x, y);
        return state.removeLast().addLast(updatedPath);
    }

}
