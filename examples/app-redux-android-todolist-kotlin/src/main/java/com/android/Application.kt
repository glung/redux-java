package com.android

import dagger.ObjectGraph

class Application : android.app.Application() {
    companion object {
        lateinit var objectGraph: ObjectGraph
    }

    override fun onCreate() {
        super.onCreate()

        objectGraph = ObjectGraph.create(ApplicationModule(this))
    }

}
