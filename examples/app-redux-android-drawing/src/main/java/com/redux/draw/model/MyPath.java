package com.redux.draw.model;

import com.google.common.primitives.Floats;

import static com.redux.utils.Preconditions.checkState;


public class MyPath {

    public static MyPath create() {
        return new MyPath(new float[0], new float[0]);
    }

    public final float[] xAxes;
    public final float[] yAxes;

    public MyPath(float[] xAxes, float[] yAxes) {
        checkState(xAxes.length == yAxes.length, "X and Y axes don't have the same size");

        this.xAxes = xAxes;
        this.yAxes = yAxes;
    }

    public MyPath append(float[] x, float[] y) {
        return new MyPath(Floats.concat(xAxes, x), Floats.concat(yAxes, y));
    }
}
