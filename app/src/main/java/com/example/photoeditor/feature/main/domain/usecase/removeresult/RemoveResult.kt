package com.example.photoeditor.feature.main.domain.usecase.removeresult

import com.example.photoeditor.shared.domain.usecase.ExecutionThread
import com.example.photoeditor.shared.domain.usecase.UseCaseCompletable
import io.reactivex.Completable
import javax.inject.Inject
import javax.inject.Named

class RemoveResult @Inject constructor(
    @Named("worker_execution_thread")
    workerThreadExecutor: ExecutionThread,
    @Named("post_execution_thread")
    postThreadExecutor: ExecutionThread,
    private val repository: RemoveResultRepository
) : UseCaseCompletable<Long>(workerThreadExecutor, postThreadExecutor) {

    override fun buildUseCaseObservable(params: Long): Completable {
        return repository.removeResult(params)
    }
}