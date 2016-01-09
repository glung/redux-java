package com.android

import com.android.todolist.R
import com.redux.TodoListAction
import com.redux.devtools.DevToolPresenter
import com.redux.TodoListState

import javax.inject.Inject

public class MainPresenter @Inject constructor(private val devToolPresenter: DevToolPresenter<TodoListAction, TodoListState>) {

    public fun bind(activity: BaseActivity) = devToolPresenter.bind(activity.findViewById(R.id.dev_tools_drawer))
}
