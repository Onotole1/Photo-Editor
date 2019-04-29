package com.example.photoeditor.core.dagger.module

import android.graphics.Bitmap
import android.net.Uri
import com.example.photoeditor.feature.main.domain.usecase.GetBitmapFromUri
import com.example.photoeditor.shared.domain.usecase.UseCase
import dagger.Binds
import dagger.Module
import javax.inject.Named

@Module
interface UseCaseModule {

    @Named("get_bitmap_from_uri")
    @Binds
    fun provideGetBitmapFromUri(getBitmapFromUri: GetBitmapFromUri): UseCase<Bitmap, Uri>

}