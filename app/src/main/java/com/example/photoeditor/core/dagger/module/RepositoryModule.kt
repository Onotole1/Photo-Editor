package com.example.photoeditor.core.dagger.module

import com.example.photoeditor.feature.main.data.repository.getbitmapfromuri.BitmapFromUriRepositoryImpl
import com.example.photoeditor.feature.main.data.repository.getexif.GetExifRepositoryImpl
import com.example.photoeditor.feature.main.data.repository.getresults.GetresultsRepositoryImpl
import com.example.photoeditor.feature.main.data.repository.removeresult.RemoveResultRepositoryImpl
import com.example.photoeditor.feature.main.data.repository.setcontrollerimage.SetControllerImageRepositoryImpl
import com.example.photoeditor.feature.main.data.repository.transform.receiver.TransformReceiverImpl
import com.example.photoeditor.feature.main.domain.usecase.getbitmapfromuri.BitmapFromUriRepository
import com.example.photoeditor.feature.main.domain.usecase.getexif.GetExifRepository
import com.example.photoeditor.feature.main.domain.usecase.getresults.GetResultsRepository
import com.example.photoeditor.feature.main.domain.usecase.removeresult.RemoveResultRepository
import com.example.photoeditor.feature.main.domain.usecase.setcontrollerimage.SetControllerImageRepository
import com.example.photoeditor.feature.main.domain.usecase.transform.TransformReceiver
import dagger.Binds
import dagger.Module

@Module(includes = [DataSourceModule::class])
interface RepositoryModule {

        @Binds
        fun provideBitmapFromUriRepository(bitmapFromUriRepository: BitmapFromUriRepositoryImpl): BitmapFromUriRepository

        @Binds
        fun provideTransformRepository(transformRepository: TransformReceiverImpl): TransformReceiver

        @Binds
        fun provideGetResultsRepository(getResultsRepository: GetresultsRepositoryImpl): GetResultsRepository

        @Binds
        fun provideRemoveResultRepository(removeResultRepository: RemoveResultRepositoryImpl): RemoveResultRepository

        @Binds
        fun provideSetControllerImageRepository(setControllerImageRepository: SetControllerImageRepositoryImpl): SetControllerImageRepository

        @Binds
        fun provideExifRepository(exifRepository: GetExifRepositoryImpl): GetExifRepository
}