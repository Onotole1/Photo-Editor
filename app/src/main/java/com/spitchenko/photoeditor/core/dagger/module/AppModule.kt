package com.spitchenko.photoeditor.core.dagger.module

import dagger.Module
import dagger.android.support.AndroidSupportInjectionModule

@Module(
    includes = [
        AndroidSupportInjectionModule::class,
        ThreadExecutionModule::class,
        MainFeatureModule::class
    ]
)
interface AppModule