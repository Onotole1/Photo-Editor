package com.example.photoeditor.feature.main.domain.usecase.getresults

import com.example.photoeditor.feature.main.domain.entity.BitmapWithId
import com.example.photoeditor.shared.domain.usecase.ExecutionThread
import com.example.photoeditor.shared.domain.usecase.UseCase
import com.example.photoeditor.shared.domain.usecase.UseCaseSingle
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

class GetResults @Inject constructor(
    @Named("worker_execution_thread")
    workerThreadExecutor: ExecutionThread,
    @Named("post_execution_thread")
    postThreadExecutor: ExecutionThread,
    private val repository: GetResultsRepository
    ): UseCaseSingle<List<@JvmSuppressWildcards BitmapWithId>, Unit>(workerThreadExecutor, postThreadExecutor) {

    override fun buildUseCaseObservable(params: Unit): Single<List<BitmapWithId>> {
        return repository.getResults()
    }
}