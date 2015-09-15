package com.android.draw;

import android.graphics.Paint;

public class PaintCreator {
	static public Paint create(int color) {
		return create(color, 4, true);
	}

	private static Paint create(int color, int strokeWidth, boolean antiAllias) {
		Paint paint = new Paint();
		paint.setAntiAlias(antiAllias);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.BEVEL);
		paint.setStrokeCap(Paint.Cap.BUTT);
		paint.setColor(color);
		paint.setStrokeWidth(strokeWidth);
		return paint;
	}
}
