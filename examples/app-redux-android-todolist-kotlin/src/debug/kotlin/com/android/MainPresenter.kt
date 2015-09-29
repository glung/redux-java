package com.android

import com.android.todolist.R
import com.redux.AppAction
import com.redux.DevToolPresenter
import com.redux.AppState

import javax.inject.Inject

public class MainPresenter @Inject constructor(private val devToolPresenter: DevToolPresenter<AppAction, AppState>) {

    public fun bind(activity: BaseActivity) = devToolPresenter.bind(activity.findViewById(R.id.dev_tools_drawer))
}
