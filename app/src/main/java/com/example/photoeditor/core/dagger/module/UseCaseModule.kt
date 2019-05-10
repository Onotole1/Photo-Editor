package com.example.photoeditor.core.dagger.module

import android.graphics.Bitmap
import com.example.photoeditor.feature.main.domain.entity.BitmapWithId
import com.example.photoeditor.feature.main.domain.entity.SetImageRequest
import com.example.photoeditor.feature.main.domain.entity.UriWithId
import com.example.photoeditor.feature.main.domain.usecase.getbitmapfromuri.GetBitmapFromUri
import com.example.photoeditor.feature.main.domain.usecase.getexif.GetExif
import com.example.photoeditor.feature.main.domain.usecase.getresults.GetResults
import com.example.photoeditor.feature.main.domain.usecase.removeresult.RemoveResult
import com.example.photoeditor.feature.main.domain.usecase.setcontrollerimage.SetControllerImage
import com.example.photoeditor.feature.main.domain.usecase.transform.invertbitmap.InvertBitmap
import com.example.photoeditor.feature.main.domain.usecase.transform.mirrorbitmap.MirrorBitmap
import com.example.photoeditor.feature.main.domain.usecase.transform.rotatebitmap.RotateBitmap
import com.example.photoeditor.shared.domain.model.State
import com.example.photoeditor.shared.domain.usecase.RandomGenerator
import com.example.photoeditor.shared.domain.usecase.UseCase
import com.example.photoeditor.shared.domain.usecase.UseCaseCompletable
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
        fun provideGetBitmapFromUri(getBitmapFromUri: GetBitmapFromUri): UseCase<State<Bitmap>, UriWithId>

        @Named("rotate_bitmap")
        @Binds
        fun provideRotateBitmap(rotateBitmap: RotateBitmap): UseCase<State<Bitmap>, BitmapWithId>

        @Named("invert_bitmap")
        @Binds
        fun provideInvertBitmap(invertBitmap: InvertBitmap): UseCase<State<Bitmap>, BitmapWithId>

        @Named("mirror_bitmap")
        @Binds
        fun provideMirrorBitmap(mirrorBitmap: MirrorBitmap): UseCase<State<Bitmap>, BitmapWithId>

        @Binds
        fun provideGetResults(getResults: GetResults): UseCase<List<@JvmSuppressWildcards BitmapWithId>, Unit>

        @Binds
        fun provideRemoveResult(removeResult: RemoveResult): UseCaseCompletable<Long>

        @Binds
        fun provideSetControllerImage(setControllerImage: SetControllerImage): UseCaseCompletable<SetImageRequest>

        @Binds
        fun provideGetExif(getExif: GetExif): UseCase<@JvmSuppressWildcards Map<String, String>, Unit>
    }
}