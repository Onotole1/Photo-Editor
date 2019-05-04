package com.example.photoeditor.feature.main.domain.usecase.getbitmapfromuri

import android.graphics.Bitmap
import android.net.Uri
import com.example.photoeditor.shared.domain.usecase.ExecutionThread
import com.example.photoeditor.shared.domain.usecase.UseCase
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Named

class GetBitmapFromUri @Inject constructor(
    private val repository: BitmapFromUriRepository,
    @Named("worker_execution_thread")
    workerThreadExecutor: ExecutionThread,
    @Named("post_execution_thread")
    postThreadExecutor: ExecutionThread
) : UseCase<Bitmap, Uri>(
    workerThreadExecutor,
    postThreadExecutor
) {
    override fun buildUseCaseObservable(params: Uri): Observable<Bitmap> {
        return Observable.fromCallable {
            repository.getBitmapFromUri(params)
        }
    }

}