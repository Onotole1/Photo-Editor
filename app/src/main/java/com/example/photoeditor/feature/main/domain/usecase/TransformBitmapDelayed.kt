package com.example.photoeditor.feature.main.domain.usecase

import android.graphics.Bitmap
import com.example.photoeditor.shared.domain.model.State
import com.example.photoeditor.shared.domain.usecase.ExecutionThread
import com.example.photoeditor.shared.domain.usecase.RandomGenerator
import com.example.photoeditor.shared.domain.usecase.UseCase
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import java.util.concurrent.TimeUnit

abstract class TransformBitmapDelayed(
    private val randomGenerator: RandomGenerator<Long>,
    workerThreadExecutor: ExecutionThread,
    postThreadExecutor: ExecutionThread
) : UseCase<State<Bitmap>, Bitmap>(workerThreadExecutor, postThreadExecutor) {

    abstract fun bitmapSource(params: Bitmap): Observable<Bitmap>

    override fun buildUseCaseObservable(params: Bitmap): Observable<State<Bitmap>> {
        val random = randomGenerator.generate()
        val max = random.dec()

        val interval = Observable.interval(1, TimeUnit.SECONDS).take(random)

        return Observable.combineLatest(
            interval,
            bitmapSource(params),
            BiFunction<Long, Bitmap, State<Bitmap>> { seconds: Long, bitmap: Bitmap ->
                if (max == seconds) {
                    State.Data(bitmap)
                } else {
                    val progress = seconds / random.toFloat() * 100F

                    State.Progress(progress.toInt())
                }
            })
    }
}