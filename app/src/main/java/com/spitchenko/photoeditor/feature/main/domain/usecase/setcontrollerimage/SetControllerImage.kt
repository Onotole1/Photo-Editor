package com.spitchenko.photoeditor.feature.main.domain.usecase.setcontrollerimage

import com.spitchenko.photoeditor.feature.main.domain.entity.SetImageRequest
import com.spitchenko.domain.usecase.ExecutionThread
import com.spitchenko.domain.usecase.UseCaseCompletable
import io.reactivex.Completable
import javax.inject.Inject
import javax.inject.Named

class SetControllerImage @Inject constructor(
    @Named("worker_execution_thread")
    workerThreadExecutor: ExecutionThread,
    @Named("post_execution_thread")
    postThreadExecutor: ExecutionThread,
    private val repository: SetControllerImageRepository
) : UseCaseCompletable<SetImageRequest>(
    workerThreadExecutor, postThreadExecutor
) {
    override fun buildUseCaseObservable(params: SetImageRequest): Completable {
        return repository.setControllerImage(params)
    }

}