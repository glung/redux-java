package com.android;

import com.android.draw.DrawingActivity;
import com.android.draw.DrawingView;
import com.redux.ReduxModule;

import dagger.Module;

@Module(
        includes = ReduxModule.class,
        injects = {
                DrawingActivity.class,
                DrawingView.class
        })
public class ApplicationModule {

}
