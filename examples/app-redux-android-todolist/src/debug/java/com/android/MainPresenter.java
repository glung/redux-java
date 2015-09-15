package com.android;

import com.android.todolist.R;
import com.redux.ActionCreator;
import com.redux.DevToolPresenter;
import com.redux.TodoState;

import javax.inject.Inject;

public class MainPresenter {
    private final DevToolPresenter<ActionCreator.Action, TodoState> devToolPresenter;

    @Inject
    public MainPresenter(DevToolPresenter<ActionCreator.Action, TodoState> devToolPresenter) {
        this.devToolPresenter = devToolPresenter;
    }

    public void bind(BaseActivity activity) {
        devToolPresenter.bind(activity.findViewById(R.id.dev_tools_drawer));
    }
}
