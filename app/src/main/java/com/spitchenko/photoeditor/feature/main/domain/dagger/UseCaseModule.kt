package com.spitchenko.photoeditor.feature.main.domain.dagger

import android.graphics.Bitmap
import com.spitchenko.photoeditor.feature.main.domain.entity.BitmapWithId
import com.spitchenko.photoeditor.feature.main.domain.entity.SetImageRequest
import com.spitchenko.photoeditor.feature.main.domain.entity.UriWithId
import com.spitchenko.photoeditor.feature.main.domain.usecase.getbitmapfromuri.GetBitmapFromUri
import com.spitchenko.photoeditor.feature.main.domain.usecase.getexif.GetExif
import com.spitchenko.photoeditor.feature.main.domain.usecase.getresults.GetResults
import com.spitchenko.photoeditor.feature.main.domain.usecase.removeresult.RemoveResult
import com.spitchenko.photoeditor.feature.main.domain.usecase.setcontrollerimage.SetControllerImage
import com.spitchenko.photoeditor.feature.main.domain.usecase.transform.invertbitmap.InvertBitmap
import com.spitchenko.photoeditor.feature.main.domain.usecase.transform.mirrorbitmap.MirrorBitmap
import com.spitchenko.photoeditor.feature.main.domain.usecase.transform.rotatebitmap.RotateBitmap
import com.spitchenko.domain.model.State
import com.spitchenko.photoeditor.feature.main.domain.usecase.transform.RandomGenerator
import com.spitchenko.domain.usecase.UseCase
import com.spitchenko.domain.usecase.UseCaseCompletable
import com.spitchenko.domain.usecase.UseCaseSingle
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Named
import kotlin.random.Random

@Module(includes = [UseCaseModule.BindsModule::class])
class UseCaseModule {

    @Provides
    fun provideRandomGenerator(): RandomGenerator<Long> = object :
        RandomGenerator<Long> {
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
        fun provideGetResults(getResults: GetResults): UseCaseSingle<List<@JvmSuppressWildcards BitmapWithId>, Unit>

        @Binds
        fun provideRemoveResult(removeResult: RemoveResult): UseCaseCompletable<Long>

        @Binds
        fun provideSetControllerImage(setControllerImage: SetControllerImage): UseCaseCompletable<SetImageRequest>

        @Binds
        fun provideGetExif(getExif: GetExif): UseCaseSingle<@JvmSuppressWildcards Map<String, String>, Unit>
    }
}