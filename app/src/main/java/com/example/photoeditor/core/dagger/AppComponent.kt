package com.example.photoeditor.core.dagger

import com.example.photoeditor.core.MainApplication
import com.example.photoeditor.core.dagger.module.AppModule
import com.example.photoeditor.core.dagger.scope.AppScope
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@AppScope
@Component(modules = [AndroidSupportInjectionModule::class, AppModule::class])
interface AppComponent: AndroidInjector<MainApplication> {

    @Component.Factory
    abstract class Factory: AndroidInjector.Factory<MainApplication>
}