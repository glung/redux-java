package com.android.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.android.Application;
import com.redux.Redux;
import com.redux.Store;
import com.redux.Subscriber;
import com.redux.Subscription;
import com.redux.draw.DrawingActions;
import com.redux.draw.model.MyPath;
import com.redux.storage.StateTree;

import javax.inject.Inject;

public class DrawingView extends View implements Subscriber {
    private final Paint paint = PaintCreator.create(Color.BLACK);

    @Inject Store<Redux.MyAction, StateTree> store;
    private Subscription subscription;

    public DrawingView(Context context) {
        super(context);
        setUp();
    }

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUp();
    }

    public DrawingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setUp();
    }

    private void setUp() {
        Application.getObjectGraph().inject(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                store.dispatch(new DrawingActions.NewPath());
                store.dispatch(new DrawingActions.AddPointsToPath(event.getX(), event.getY()));
                return true;
            case MotionEvent.ACTION_MOVE:
                final int nbPoints = event.getHistorySize() + 1;
                float[] xAxes = new float[nbPoints];
                float[] yAxes = new float[nbPoints];

                for (int i = 0, size = event.getHistorySize(); i < size; i++) {
                    xAxes[i] = event.getHistoricalX(i);
                    yAxes[i] = event.getHistoricalY(i);
                }
                xAxes[nbPoints - 1] = event.getX();
                yAxes[nbPoints - 1] = event.getY();

                store.dispatch(new DrawingActions.AddPointsToPath(xAxes, yAxes));
                return true;
            case MotionEvent.ACTION_UP:
                store.dispatch(new DrawingActions.AddPointsToPath(event.getX(), event.getY()));
                store.dispatch(new DrawingActions.InterpolatePath());
                return true;
            default:
                return false;
        }
    }

    @Override protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        subscription = store.subscribe(this);
    }

    @Override protected void onDetachedFromWindow() {
        subscription.unsubscribe();
        super.onDetachedFromWindow();
    }

    @Override public void onStateChanged() {
        invalidate();
    }

    @Override public void draw(Canvas canvas) {
        super.draw(canvas);
        for (MyPath myPath : store.getState().get()) {
            canvas.drawPath(createPath(myPath), paint);
        }
    }

    private Path createPath(MyPath myPath) {
        final float[] xAxes = myPath.xAxes;
        final float[] yAxes = myPath.yAxes;

        final Path path = new Path();
        if (xAxes.length < 2) {
            return path;
        }

        path.moveTo(xAxes[0], yAxes[0]);
        for (int i = 1, size = xAxes.length; i < size; i++) {
            path.lineTo(xAxes[i], yAxes[i]);
        }

        return path;
    }
}
