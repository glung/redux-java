package com.redux.draw.interpolation;

public class NatCubicContext {
	private final double density;

	public NatCubicContext(double pixelPerPoints) {
		density = pixelPerPoints;
	}

	public int getInterpolationSize(Cubic xAxisCubic, Cubic yAxisCubic) {
		final float x1 = xAxisCubic.eval(0);
		final float y1 = yAxisCubic.eval(0);
		final float x2 = xAxisCubic.eval(1);
		final float y2 = yAxisCubic.eval(1);
		double length = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
		return (int) Math.ceil(length * density);
	}
}
