package com.spitchenko.domain.usecase

import io.reactivex.Scheduler

interface ExecutionThread {
    val scheduler: Scheduler
}