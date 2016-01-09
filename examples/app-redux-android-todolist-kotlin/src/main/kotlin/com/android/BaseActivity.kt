package com.android

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import javax.inject.Inject

public open class BaseActivity : AppCompatActivity() {
    @Inject lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Application.getObjectGraph().inject(this)
    }

}
