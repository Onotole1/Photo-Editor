package com.spitchenko.test

import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class RxSchedulersTestRule(private val scheduler: Scheduler = Schedulers.trampoline()) : TestRule {

    override fun apply(base: Statement, description: Description): Statement = object : Statement() {
        override fun evaluate() {
            RxAndroidPlugins.reset()
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler }
            RxAndroidPlugins.setMainThreadSchedulerHandler { scheduler }

            RxJavaPlugins.reset()
            RxJavaPlugins.setIoSchedulerHandler { scheduler }
            RxJavaPlugins.setComputationSchedulerHandler { scheduler }
            RxJavaPlugins.setNewThreadSchedulerHandler { scheduler }
            RxJavaPlugins.setSingleSchedulerHandler { scheduler }

            try {
                base.evaluate()
            } finally {
                RxJavaPlugins.reset()
                RxAndroidPlugins.reset()
            }
        }
    }
}