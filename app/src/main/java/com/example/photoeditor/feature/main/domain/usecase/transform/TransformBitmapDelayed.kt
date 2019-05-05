package com.example.photoeditor.feature.main.domain.usecase.transform

import android.graphics.Bitmap
import com.example.photoeditor.feature.main.domain.entity.BitmapWithId
import com.example.photoeditor.shared.domain.model.State
import com.example.photoeditor.shared.domain.usecase.ExecutionThread
import com.example.photoeditor.shared.domain.usecase.RandomGenerator
import com.example.photoeditor.shared.domain.usecase.UseCase
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import java.util.concurrent.TimeUnit

abstract class TransformBitmapDelayed(
    private val randomGenerator: RandomGenerator<Long>,
    private val repository: TransformRepository,
    workerThreadExecutor: ExecutionThread,
    postThreadExecutor: ExecutionThread
) : UseCase<State<Bitmap>, BitmapWithId>(workerThreadExecutor, postThreadExecutor) {

    abstract fun bitmapSource(params: Bitmap): Observable<Bitmap>

    override fun buildUseCaseObservable(params: BitmapWithId): Observable<State<Bitmap>> {
        val random = randomGenerator.generate()
        val max = random.dec()

        val interval = Observable.interval(1, TimeUnit.SECONDS).take(random)

        return Observable.combineLatest(
            interval,
            bitmapSource(params.source).map {
                repository.saveBitmapToFile(params.copy(source = it))
                it
            },
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