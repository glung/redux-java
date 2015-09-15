package com.redux.draw;

import com.google.common.base.MoreObjects;
import com.redux.Redux;

public class DrawingActions {
    public static class NewPath extends Redux.MyAction {

        public static final String ACTION = "NEW_PATH";

        public NewPath() {
            super(ACTION);
        }

    }

    public static class AddPointsToPath extends Redux.MyAction {

        public static final String ACTION = "ADD_POINTS_TO_PATH";
        public final float[] xAxes;
        public final float[] yAxes;

        public AddPointsToPath(float[] xAxes, float[] yAxes) {
            super(ACTION);
            this.xAxes = xAxes;
            this.yAxes = yAxes;
        }

        public AddPointsToPath(float xAxes, float yAxes) {
            super(ACTION);
            this.xAxes = new float[]{xAxes};
            this.yAxes = new float[]{yAxes};
        }

        @Override public String toString() {
            return MoreObjects
                    .toStringHelper(this)
                    .add("Nb points", xAxes.length)
                    .toString();
        }

    }

    public static class InterpolatePath extends Redux.MyAction {

        public static final String ACTION = "INTERPOLATE_PATH";


        public InterpolatePath() {
            super(ACTION);
        }

    }

}
