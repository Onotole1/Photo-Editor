package com.example.photoeditor.core.dagger.module

import android.content.ContentResolver
import com.example.photoeditor.core.MainApplication
import com.example.photoeditor.feature.main.data.repository.getbitmapfromuri.BitmapFromUriRepositoryImpl
import com.example.photoeditor.feature.main.data.repository.getresults.GetresultsRepositoryImpl
import com.example.photoeditor.feature.main.data.repository.removeresult.RemoveResultRepositoryImpl
import com.example.photoeditor.feature.main.data.repository.transform.TransformRepositoryImpl
import com.example.photoeditor.feature.main.domain.usecase.getbitmapfromuri.BitmapFromUriRepository
import com.example.photoeditor.feature.main.domain.usecase.getresults.GetResultsRepository
import com.example.photoeditor.feature.main.domain.usecase.removeresult.RemoveResultRepository
import com.example.photoeditor.feature.main.domain.usecase.transform.TransformRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [DataSourceModule::class, RepositoryModule.BindsModule::class])
class RepositoryModule {

    @Provides
    fun provideContentResolver(context: MainApplication): ContentResolver = context.contentResolver

    @Module
    interface BindsModule {

        @Binds
        fun provideBitmapFromUriRepository(bitmapFromUriRepository: BitmapFromUriRepositoryImpl): BitmapFromUriRepository

        @Binds
        fun provideTransformRepository(transformRepository: TransformRepositoryImpl): TransformRepository

        @Binds
        fun provideGetResultsRepository(getResultsRepository: GetresultsRepositoryImpl): GetResultsRepository

        @Binds
        fun provideRemoveResultRepository(removeResultRepository: RemoveResultRepositoryImpl): RemoveResultRepository
    }
}