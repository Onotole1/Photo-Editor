package com.spitchenko.photoeditor.core.dagger.module

import com.spitchenko.photoeditor.core.dagger.scope.ActivityScope
import com.spitchenko.photoeditor.feature.main.data.dagger.DataModule
import com.spitchenko.photoeditor.feature.main.domain.dagger.UseCaseModule
import com.spitchenko.photoeditor.feature.main.presentation.dagger.MainActivityModule
import com.spitchenko.photoeditor.feature.main.presentation.view.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [UseCaseModule::class, DataModule::class])
interface MainFeatureModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    fun mainActivityInjector(): MainActivity
}