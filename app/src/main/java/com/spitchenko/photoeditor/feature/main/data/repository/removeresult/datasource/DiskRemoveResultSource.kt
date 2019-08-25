package com.spitchenko.photoeditor.feature.main.data.repository.removeresult.datasource

import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import java.io.File
import javax.inject.Inject
import javax.inject.Named

class DiskRemoveResultSource @Inject constructor(
    @Named("images_dir_result")
    private val imageResultPath: File
) : RemoveResultSource {

    override fun removeResult(imageId: Long): Completable = Completable.fromCallable {
        File(imageResultPath, imageId.toString()).delete()
        Unit
    }.subscribeOn(Schedulers.io())
}