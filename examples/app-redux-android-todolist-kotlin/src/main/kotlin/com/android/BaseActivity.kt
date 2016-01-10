package com.android

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

public open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Application.objectGraph.inject(this)
    }

}
