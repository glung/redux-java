package com.android;

import com.glung.android.facets.R;
import com.redux.DevToolPresenter;
import com.redux.Redux.MyAction;
import com.redux.storage.StateTree;

import javax.inject.Inject;

public class MainPresenter {
    private final DevToolPresenter<MyAction, StateTree> devToolPresenter;

    @Inject
    public MainPresenter(DevToolPresenter<MyAction, StateTree> devToolPresenter) {
        this.devToolPresenter = devToolPresenter;
    }

    public void bind(BaseActivity activity) {
        devToolPresenter.bind(activity.findViewById(R.id.dev_tools_drawer));
    }
}
