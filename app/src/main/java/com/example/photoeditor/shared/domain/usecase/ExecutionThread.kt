package com.example.photoeditor.shared.domain.usecase

import io.reactivex.Scheduler

interface ExecutionThread {
    val scheduler: Scheduler
}