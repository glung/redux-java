package com.android;

import com.android.todolist.R;
import com.redux.ActionCreator;
import com.redux.AppState;
import com.redux.DevToolPresenter;

import javax.inject.Inject;

public class MainPresenter {
    private final DevToolPresenter<ActionCreator.Action, AppState> devToolPresenter;

    @Inject
    public MainPresenter(DevToolPresenter<ActionCreator.Action, AppState> devToolPresenter) {
        this.devToolPresenter = devToolPresenter;
    }

    public void bind(BaseActivity activity) {
        devToolPresenter.bind(activity.findViewById(R.id.dev_tools_drawer));
    }
}
