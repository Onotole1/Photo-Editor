package com.spitchenko.photoeditor.feature.main.domain.usecase.transform

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.test.platform.app.InstrumentationRegistry
import com.spitchenko.photoeditor.feature.main.domain.entity.BitmapWithId
import com.spitchenko.photoeditor.feature.main.domain.usecase.getexif.GetExifRepository
import com.spitchenko.photoeditor.feature.main.domain.usecase.transform.invertbitmap.InvertBitmap
import com.spitchenko.photoeditor.feature.main.domain.usecase.transform.mirrorbitmap.MirrorBitmap
import com.spitchenko.photoeditor.feature.main.domain.usecase.transform.rotatebitmap.RotateBitmap
import com.spitchenko.domain.model.State
import com.spitchenko.domain.usecase.ExecutionThread
import com.spitchenko.domain.usecase.UseCase
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import org.junit.Test
import java.util.concurrent.TimeUnit

class TransformBitmapTest {

    private val testExecutionThread = object : ExecutionThread {
        override val scheduler: Scheduler = Schedulers.trampoline()
    }

    private val testScheduler = TestScheduler()

    private val testTimerExecutionThread = object : ExecutionThread {
        override val scheduler: Scheduler = testScheduler
    }

    private val randomGenerator = object : RandomGenerator<Long> {
        override fun generate(): Long = DELAY
    }

    private val transformRepository = object : TransformReceiver {
        override fun saveBitmapToFile(params: BitmapWithId, exif: Map<String, String>) {
            // do nothing
        }
    }

    private val exifRepository = object : GetExifRepository {
        override fun getExif(): Single<Map<String, String>> {
            return Single.just(emptyMap())
        }
    }

    @Test
    fun bitmap_rotation_test() {
        val rotateBitmap = RotateBitmap(
            testTimerExecutionThread,
            testExecutionThread,
            testExecutionThread,
            randomGenerator,
            transformRepository,
            exifRepository
        )

        bitmap_transform_test("original.png", "rotate.png", rotateBitmap)
    }

    @Test
    fun bitmap_mirror_test() {
        val mirrorBitmap = MirrorBitmap(
            testTimerExecutionThread,
            testExecutionThread,
            testExecutionThread,
            randomGenerator,
            transformRepository,
            exifRepository
        )

        bitmap_transform_test("original.png", "mirror.png", mirrorBitmap)
    }

    @Test
    fun bitmap_invert_test() {
        val mirrorBitmap = InvertBitmap(
            testTimerExecutionThread,
            testExecutionThread,
            testExecutionThread,
            randomGenerator,
            transformRepository,
            exifRepository
        )

        bitmap_transform_test("original.png", "invert.png", mirrorBitmap)
    }

    private fun bitmap_transform_test(
        originalFileName: String, expectedFileName: String,
        useCase: UseCase<State<Bitmap>, BitmapWithId>
    ) {
        val context = InstrumentationRegistry.getInstrumentation().context

        val original = context.assets.open(originalFileName).use {
            BitmapFactory.decodeStream(it)
        }

        val expected = context.assets.open(expectedFileName).use {
            BitmapFactory.decodeStream(it)
        }

        val observer = TestObserver<State<Bitmap>>()

        useCase.execute(observer, BitmapWithId(0, original))

        testScheduler.advanceTimeBy(DELAY, TimeUnit.SECONDS)

        observer.assertSubscribed()

        observer.awaitTerminalEvent(DELAY, TimeUnit.SECONDS)

        observer.assertValue {
            it is State.Data && it.data.sameAs(expected)
        }
    }

    private companion object {
        const val DELAY = 1L
    }
}