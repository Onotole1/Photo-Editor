package com.example.photoeditor.core.dagger.module

import com.spitchenko.domain.usecase.ExecutionThread
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Named

@Module
class ThreadExecutionModule {

    @Named("worker_execution_thread")
    @Provides
    fun provideWorkerExecutionThread(): ExecutionThread = object : ExecutionThread {
        override val scheduler: Scheduler = Schedulers.io()
    }

    @Named("post_execution_thread")
    @Provides
    fun providePostExecutionThread(): ExecutionThread = object : ExecutionThread {
        override val scheduler: Scheduler = AndroidSchedulers.mainThread()
    }
}