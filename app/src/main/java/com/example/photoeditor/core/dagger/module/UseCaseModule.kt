package com.example.photoeditor.core.dagger.module

import android.graphics.Bitmap
import android.net.Uri
import com.example.photoeditor.feature.main.domain.usecase.getbitmap.GetBitmapFromUri
import com.example.photoeditor.feature.main.domain.usecase.invertbitmap.InvertBitmap
import com.example.photoeditor.feature.main.domain.usecase.mirrorbitmap.MirrorBitmap
import com.example.photoeditor.feature.main.domain.usecase.rotatebitmap.RotateBitmap
import com.example.photoeditor.shared.domain.model.State
import com.example.photoeditor.shared.domain.usecase.RandomGenerator
import com.example.photoeditor.shared.domain.usecase.UseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Named
import kotlin.random.Random

@Module(includes = [UseCaseModule.BindsModule::class])
class UseCaseModule {

    @Provides
    fun provideRandomGenerator(): RandomGenerator<Long> = object : RandomGenerator<Long> {
        override fun generate(): Long = Random.nextLong(5, 30)
    }

    @Module
    interface BindsModule {
        @Binds
        fun provideGetBitmapFromUri(getBitmapFromUri: GetBitmapFromUri): UseCase<Bitmap, Uri>

        @Named("rotate_bitmap")
        @Binds
        fun provideRotateBitmap(rotateBitmap: RotateBitmap): UseCase<State<Bitmap>, Bitmap>

        @Named("invert_bitmap")
        @Binds
        fun provideInvertBitmap(invertBitmap: InvertBitmap): UseCase<State<Bitmap>, Bitmap>

        @Named("mirror_bitmap")
        @Binds
        fun provideMirrorBitmap(mirrorBitmap: MirrorBitmap): UseCase<State<Bitmap>, Bitmap>
    }
}