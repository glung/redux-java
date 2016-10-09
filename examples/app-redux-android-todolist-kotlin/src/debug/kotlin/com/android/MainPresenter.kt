package com.android

import com.android.todolist.R
import com.redux.AppAction
import com.redux.AppState
import com.redux.DevToolPresenter
import javax.inject.Inject

class MainPresenter @Inject constructor(private val devToolPresenter: DevToolPresenter<AppAction, AppState>) {

    fun bind(activity: BaseActivity) = devToolPresenter.bind(activity.findViewById(R.id.dev_tools_drawer))
}
