package com.spitchenko.photoeditor.core.dagger.module

import com.spitchenko.photoeditor.core.dagger.scope.ActivityScope
import com.spitchenko.photoeditor.feature.main.dagger.MainActivityModule
import com.spitchenko.photoeditor.feature.main.presentation.view.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Module(
    includes = [
        AndroidSupportInjectionModule::class,
        ThreadExecutionModule::class,
        UseCaseModule::class,
        DataModule::class
    ]
)
interface AppModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    fun mainActivityInjector(): MainActivity
}