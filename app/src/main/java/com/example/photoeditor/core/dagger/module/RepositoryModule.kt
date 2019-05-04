package com.example.photoeditor.core.dagger.module

import com.example.photoeditor.core.MainApplication
import com.example.photoeditor.feature.main.data.entity.ReqBitmapSize
import com.example.photoeditor.feature.main.data.repository.getbitmapfromuri.BitmapFromUriRepositoryImpl
import com.example.photoeditor.feature.main.data.repository.getbitmapfromurl.BitmapFromUrlRepositoryImpl
import com.example.photoeditor.feature.main.domain.usecase.getbitmapfromuri.BitmapFromUriRepository
import com.example.photoeditor.feature.main.domain.usecase.getbitmapfromurl.BitmapFromUrlRepository
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
    }
}