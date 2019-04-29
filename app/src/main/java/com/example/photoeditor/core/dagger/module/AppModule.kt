package com.example.photoeditor.core.dagger.module

import com.example.photoeditor.core.dagger.scope.ActivityScope
import com.example.photoeditor.feature.main.dagger.MainActivityModule
import com.example.photoeditor.feature.main.presentation.view.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Module(
    includes = [
        AndroidSupportInjectionModule::class,
        ThreadExecutionModule::class,
        UseCaseModule::class,
        RepositoryModule::class
    ]
)
interface AppModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    fun mainActivityInjector(): MainActivity
}