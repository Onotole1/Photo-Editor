package com.spitchenko.photoeditor.feature.main.domain.usecase.transform

import android.graphics.BitmapFactory
import androidx.exifinterface.media.ExifInterface
import androidx.test.platform.app.InstrumentationRegistry
import com.nhaarman.mockitokotlin2.*
import com.spitchenko.domain.model.State
import com.spitchenko.photoeditor.BuildConfig
import com.spitchenko.photoeditor.feature.main.domain.entity.BitmapWithId
import com.spitchenko.photoeditor.feature.main.domain.usecase.getexif.GetExifRepository
import com.spitchenko.photoeditor.feature.main.domain.usecase.transform.invertbitmap.InvertBitmapImpl
import com.spitchenko.photoeditor.feature.main.domain.usecase.transform.mirrorbitmap.MirrorBitmapImpl
import com.spitchenko.photoeditor.feature.main.domain.usecase.transform.rotatebitmap.RotateBitmapImpl
import com.spitchenko.test.RxSchedulersTestRule
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class TransformBitmapTest {

    @get:Rule
    var rxSchedulersTestRule: TestRule = RxSchedulersTestRule()

    private val randomGenerator = object : RandomGenerator<Long> {
        override fun generate(): Long = DELAY
    }

    private val transformReceiver: TransformReceiver = mock()
    private val exifRepository: GetExifRepository = mock()
    private val randomSource: RandomSource = mock()

    private val delegate =
        TransformBitmapDelayedDelegate(
            randomGenerator,
            randomSource,
            transformReceiver,
            exifRepository
        )

    @Test
    fun bitmap_rotation_test() {
        whenever(exifRepository.getExif()).thenReturn(Single.just(emptyMap()))
        whenever(randomSource(DELAY)).thenReturn(Observable.fromIterable(0..DELAY.dec()))

        val rotateBitmap = RotateBitmapImpl(
            delegate
        )

        val context = InstrumentationRegistry.getInstrumentation().context

        val original = context.assets.open("original.png").use {
            BitmapFactory.decodeStream(it)
        }

        val expected = context.assets.open("rotate.png").use {
            BitmapFactory.decodeStream(it)
        }

        val expectedExif = mapOf(
            ExifInterface.TAG_IMAGE_LENGTH to expected.height.toString(),
            ExifInterface.TAG_IMAGE_WIDTH to expected.width.toString(),
            ExifInterface.TAG_MODEL to BuildConfig.APPLICATION_ID
        )

        rotateBitmap(BitmapWithId(TEST_IMAGE_ID, original))
            .test()
            .assertValue {
                it is State.Data && it.data.sameAs(expected)
            }

        verify(exifRepository).getExif()
        verify(transformReceiver).saveBitmapToFile(any(), eq(expectedExif))
        verify(randomSource).invoke(DELAY)
    }

    @Test
    fun bitmap_mirror_test() {
        whenever(exifRepository.getExif()).thenReturn(Single.just(emptyMap()))
        whenever(randomSource(DELAY)).thenReturn(Observable.fromIterable(0..DELAY.dec()))

        val mirrorBitmap = MirrorBitmapImpl(
            delegate
        )

        val context = InstrumentationRegistry.getInstrumentation().context

        val original = context.assets.open("original.png").use {
            BitmapFactory.decodeStream(it)
        }

        val expected = context.assets.open("mirror.png").use {
            BitmapFactory.decodeStream(it)
        }

        val expectedExif = mapOf(
            ExifInterface.TAG_IMAGE_LENGTH to expected.height.toString(),
            ExifInterface.TAG_IMAGE_WIDTH to expected.width.toString(),
            ExifInterface.TAG_MODEL to BuildConfig.APPLICATION_ID
        )

        mirrorBitmap(BitmapWithId(TEST_IMAGE_ID, original))
            .test()
            .assertValue {
                it is State.Data && it.data.sameAs(expected)
            }

        verify(exifRepository).getExif()
        verify(transformReceiver).saveBitmapToFile(any(), eq(expectedExif))
        verify(randomSource).invoke(DELAY)
    }

    @Test
    fun bitmap_invert_test() {
        whenever(exifRepository.getExif()).thenReturn(Single.just(emptyMap()))
        whenever(randomSource(DELAY)).thenReturn(Observable.fromIterable(0..DELAY.dec()))

        val mirrorBitmap = InvertBitmapImpl(
            delegate
        )

        val context = InstrumentationRegistry.getInstrumentation().context

        val original = context.assets.open("original.png").use {
            BitmapFactory.decodeStream(it)
        }

        val expected = context.assets.open("invert.png").use {
            BitmapFactory.decodeStream(it)
        }

        val expectedExif = mapOf(
            ExifInterface.TAG_IMAGE_LENGTH to expected.height.toString(),
            ExifInterface.TAG_IMAGE_WIDTH to expected.width.toString(),
            ExifInterface.TAG_MODEL to BuildConfig.APPLICATION_ID
        )

        mirrorBitmap(BitmapWithId(TEST_IMAGE_ID, original))
            .test()
            .assertValue {
                it is State.Data && it.data.sameAs(expected)
            }

        verify(exifRepository).getExif()
        verify(transformReceiver).saveBitmapToFile(any(), eq(expectedExif))
        verify(randomSource).invoke(DELAY)
    }

    private companion object {
        const val DELAY = 1000L
        const val TEST_IMAGE_ID = 0L
    }
}