/**
 *
 */
package com.android.draw;

import android.os.Bundle;

import com.android.BaseActivity;
import com.glung.android.facets.R;
import com.redux.Redux;
import com.redux.Store;
import com.redux.storage.StateTree;

import javax.inject.Inject;


public class DrawingActivity extends BaseActivity {
    @Inject Store<Redux.MyAction, StateTree> devTool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawing);
    }

}
