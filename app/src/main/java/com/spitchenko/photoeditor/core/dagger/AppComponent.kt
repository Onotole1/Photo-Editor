package com.spitchenko.photoeditor.core.dagger

import com.spitchenko.photoeditor.core.MainApplication
import com.spitchenko.photoeditor.core.dagger.module.AppModule
import com.spitchenko.photoeditor.core.dagger.scope.AppScope
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@AppScope
@Component(modules = [AndroidSupportInjectionModule::class, AppModule::class])
interface AppComponent: AndroidInjector<MainApplication> {

    @Component.Factory
    abstract class Factory: AndroidInjector.Factory<MainApplication>
}