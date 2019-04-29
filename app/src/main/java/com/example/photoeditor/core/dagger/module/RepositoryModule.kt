package com.example.photoeditor.core.dagger.module

import com.example.photoeditor.core.MainApplication
import com.example.photoeditor.feature.main.data.repository.BitmapFromUriRepositoryImpl
import com.example.photoeditor.feature.main.domain.usecase.getbitmap.BitmapFromUriRepository
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    fun provideBitmapRepository(context: MainApplication): BitmapFromUriRepository {
        return BitmapFromUriRepositoryImpl(context)
    }
}