package com.redux.devtools;

import android.graphics.Color;

class ColorBuilder {

    private static final int MAX_HUE = 120;
    private static final float SATURATION = 60;
    private static final float VALUE = 40;

    int forValue(float number, long max) {
        float latencyPer120 = Math.min(number * MAX_HUE / max, MAX_HUE);

        final int alpha = 150 ;
        final float hue = MAX_HUE - latencyPer120;
        float[] hsv = new float[]{hue, SATURATION, VALUE};
        return Color.HSVToColor(alpha, hsv);
    }

}
