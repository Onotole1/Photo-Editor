package com.example.photoeditor.core

import com.example.photoeditor.core.dagger.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class MainApplication: DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }
}