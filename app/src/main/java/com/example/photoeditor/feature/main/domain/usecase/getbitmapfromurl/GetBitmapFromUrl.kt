package com.example.photoeditor.feature.main.domain.usecase.getbitmapfromurl

import android.graphics.Bitmap
import com.example.photoeditor.shared.domain.model.State
import com.example.photoeditor.shared.domain.usecase.ExecutionThread
import com.example.photoeditor.shared.domain.usecase.UseCase
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Named

class GetBitmapFromUrl @Inject constructor(
    private val repository: BitmapFromUrlRepository,
    @Named("worker_execution_thread")
    workerThreadExecutor: ExecutionThread,
    @Named("post_execution_thread")
    postThreadExecutor: ExecutionThread
) : UseCase<State<Bitmap>, String>(
    workerThreadExecutor,
    postThreadExecutor
) {
    override fun buildUseCaseObservable(params: String): Observable<State<Bitmap>> {
        return repository.getBitmapFromUrl(params)
    }
}