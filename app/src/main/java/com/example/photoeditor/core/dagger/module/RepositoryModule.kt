package com.example.photoeditor.core.dagger.module

import com.example.photoeditor.core.MainApplication
import com.example.photoeditor.feature.main.data.entity.ReqBitmapSize
import com.example.photoeditor.feature.main.data.repository.getbitmapfromuri.BitmapFromUriRepositoryImpl
import com.example.photoeditor.feature.main.data.repository.getbitmapfromurl.BitmapFromUrlRepositoryImpl
import com.example.photoeditor.feature.main.data.repository.getresults.GetresultsRepositoryImpl
import com.example.photoeditor.feature.main.data.repository.removeresult.RemoveResultRepositoryImpl
import com.example.photoeditor.feature.main.data.repository.transform.TransformRepositoryImpl
import com.example.photoeditor.feature.main.domain.usecase.getbitmapfromuri.BitmapFromUriRepository
import com.example.photoeditor.feature.main.domain.usecase.getbitmapfromurl.BitmapFromUrlRepository
import com.example.photoeditor.feature.main.domain.usecase.getresults.GetResultsRepository
import com.example.photoeditor.feature.main.domain.usecase.removeresult.RemoveResultRepository
import com.example.photoeditor.feature.main.domain.usecase.transform.TransformRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [DataSourceModule::class, RepositoryModule.BindsModule::class])
class RepositoryModule {

    @Provides
    fun provideBitmapRepository(context: MainApplication, reqBitmapSize: ReqBitmapSize): BitmapFromUriRepository {
        return BitmapFromUriRepositoryImpl(context.contentResolver, reqBitmapSize)
    }

    @Module
    interface BindsModule {

        @Binds
        fun provideBitmapFromUrlRepository(bitmapFromUrlRepository: BitmapFromUrlRepositoryImpl): BitmapFromUrlRepository

        @Binds
        fun provideTransformRepository(transformRepository: TransformRepositoryImpl): TransformRepository

        @Binds
        fun provideGetResultsRepository(getResultsRepository: GetresultsRepositoryImpl): GetResultsRepository

        @Binds
        fun provideRemoveResultRepository(removeResultRepository: RemoveResultRepositoryImpl): RemoveResultRepository
    }
}