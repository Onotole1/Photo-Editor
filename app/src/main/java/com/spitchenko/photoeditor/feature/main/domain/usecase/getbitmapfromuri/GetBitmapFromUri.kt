package com.spitchenko.photoeditor.feature.main.domain.usecase.getbitmapfromuri

import android.graphics.Bitmap
import com.spitchenko.photoeditor.feature.main.domain.entity.UriWithId
import com.spitchenko.domain.model.State
import com.spitchenko.domain.usecase.ExecutionThread
import com.spitchenko.domain.usecase.UseCase
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Named

class GetBitmapFromUri @Inject constructor(
    private val repository: BitmapFromUriRepository,
    @Named("worker_execution_thread")
    workerThreadExecutor: ExecutionThread,
    @Named("post_execution_thread")
    postThreadExecutor: ExecutionThread
) : UseCase<State<Bitmap>, UriWithId>(
    workerThreadExecutor,
    postThreadExecutor
) {
    override fun buildUseCaseObservable(params: UriWithId): Observable<State<Bitmap>> {
        return repository.getBitmapFromUri(params)
    }

}