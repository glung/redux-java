package com.android;

import com.android.todolist.R;
import com.redux.AppAction;
import com.redux.DevToolPresenter;
import com.redux.AppState;

import javax.inject.Inject;

public class MainPresenter {
    private final DevToolPresenter<AppAction, AppState> devToolPresenter;

    @Inject
    public MainPresenter(DevToolPresenter<AppAction, AppState> devToolPresenter) {
        this.devToolPresenter = devToolPresenter;
    }

    public void bind(BaseActivity activity) {
        devToolPresenter.bind(activity.findViewById(R.id.dev_tools_drawer));
    }
}
