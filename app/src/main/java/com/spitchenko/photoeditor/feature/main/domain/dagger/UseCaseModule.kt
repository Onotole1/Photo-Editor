package com.spitchenko.photoeditor.feature.main.domain.dagger

import com.spitchenko.photoeditor.feature.main.domain.usecase.getbitmapfromuri.GetBitmapFromUri
import com.spitchenko.photoeditor.feature.main.domain.usecase.getbitmapfromuri.GetBitmapFromUriImpl
import com.spitchenko.photoeditor.feature.main.domain.usecase.getexif.GetExif
import com.spitchenko.photoeditor.feature.main.domain.usecase.getexif.GetExifImpl
import com.spitchenko.photoeditor.feature.main.domain.usecase.getresults.GetResults
import com.spitchenko.photoeditor.feature.main.domain.usecase.getresults.GetResultsImpl
import com.spitchenko.photoeditor.feature.main.domain.usecase.removeresult.RemoveResult
import com.spitchenko.photoeditor.feature.main.domain.usecase.removeresult.RemoveResultImpl
import com.spitchenko.photoeditor.feature.main.domain.usecase.setcontrollerimage.SetControllerImage
import com.spitchenko.photoeditor.feature.main.domain.usecase.setcontrollerimage.SetControllerImageImpl
import com.spitchenko.photoeditor.feature.main.domain.usecase.transform.RandomGenerator
import com.spitchenko.photoeditor.feature.main.domain.usecase.transform.RandomSource
import com.spitchenko.photoeditor.feature.main.domain.usecase.transform.RandomSourceImpl
import com.spitchenko.photoeditor.feature.main.domain.usecase.transform.invertbitmap.InvertBitmap
import com.spitchenko.photoeditor.feature.main.domain.usecase.transform.invertbitmap.InvertBitmapImpl
import com.spitchenko.photoeditor.feature.main.domain.usecase.transform.mirrorbitmap.MirrorBitmap
import com.spitchenko.photoeditor.feature.main.domain.usecase.transform.mirrorbitmap.MirrorBitmapImpl
import com.spitchenko.photoeditor.feature.main.domain.usecase.transform.rotatebitmap.RotateBitmap
import com.spitchenko.photoeditor.feature.main.domain.usecase.transform.rotatebitmap.RotateBitmapImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
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
        fun provideGetBitmapFromUri(getBitmapFromUri: GetBitmapFromUriImpl): GetBitmapFromUri

        @Binds
        fun provideRotateBitmap(rotateBitmap: RotateBitmapImpl): RotateBitmap

        @Binds
        fun provideInvertBitmap(invertBitmap: InvertBitmapImpl): InvertBitmap

        @Binds
        fun provideMirrorBitmap(mirrorBitmap: MirrorBitmapImpl): MirrorBitmap

        @Binds
        fun provideGetResults(getResults: GetResultsImpl): GetResults

        @Binds
        fun provideRemoveResult(removeResult: RemoveResultImpl): RemoveResult

        @Binds
        fun provideSetControllerImage(setControllerImage: SetControllerImageImpl): SetControllerImage

        @Binds
        fun provideGetExif(getExif: GetExifImpl): GetExif

        @Binds
        fun bindRandomSource(randomSourceImpl: RandomSourceImpl): RandomSource
    }
}