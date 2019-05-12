package com.spitchenko.photoeditor.core.dagger.module

import android.content.res.Resources
import com.spitchenko.photoeditor.feature.main.data.entity.ReqBitmapSize
import dagger.Module
import dagger.Provides

@Module(includes = [RepositoryModule::class])
class DataModule {

    @Provides
    fun provideReqBitmapSize(): ReqBitmapSize {
        val reqSize = Resources.getSystem().displayMetrics.widthPixels / 2
        return ReqBitmapSize(reqSize, reqSize)
    }
}