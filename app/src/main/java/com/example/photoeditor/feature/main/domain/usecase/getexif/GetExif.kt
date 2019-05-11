package com.example.photoeditor.feature.main.domain.usecase.getexif

import com.spitchenko.domain.usecase.ExecutionThread
import com.spitchenko.domain.usecase.UseCaseSingle
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

class GetExif @Inject constructor(
    @Named("worker_execution_thread")
    workerThreadExecutor: ExecutionThread,
    @Named("post_execution_thread")
    postThreadExecutor: ExecutionThread,
    private val repository: GetExifRepository
) : UseCaseSingle<@JvmSuppressWildcards Map<String, String>, Unit>(
    workerThreadExecutor, postThreadExecutor
) {
    override fun buildUseCaseObservable(params: Unit): Single<Map<String, String>> {
        return repository.getExif()
    }
}