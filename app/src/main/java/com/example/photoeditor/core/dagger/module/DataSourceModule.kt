package com.example.photoeditor.core.dagger.module

import com.example.photoeditor.core.MainApplication
import com.example.photoeditor.feature.main.data.repository.getbitmapfromuri.datasource.BitmapDataSource
import com.example.photoeditor.feature.main.data.repository.getbitmapfromuri.datasource.DiskBitmapDataSource
import com.example.photoeditor.feature.main.data.repository.getbitmapfromuri.datasource.NetworkBitmapDataSource
import com.example.photoeditor.feature.main.data.repository.getresults.datasource.DiskResultsDataSource
import com.example.photoeditor.feature.main.data.repository.getresults.datasource.ResultsDataSource
import com.example.photoeditor.feature.main.data.repository.removeresult.datasource.DiskRemoveResultSource
import com.example.photoeditor.feature.main.data.repository.removeresult.datasource.RemoveResultSource
import com.example.photoeditor.feature.main.data.repository.setcontrollerimage.datasource.DiskSetControllerImageSource
import com.example.photoeditor.feature.main.data.repository.setcontrollerimage.datasource.SetControllerImageSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import java.io.File
import javax.inject.Named

@Module(includes = [DataSourceModule.BindsModule::class])
class DataSourceModule {

    @Named("images_dir_result")
    @Provides
    fun provideImagesPathResult(context: MainApplication): File {
        return File(context.filesDir, "result").apply {
            mkdirs()
        }
    }

    @Named("controller_image_dir")
    @Provides
    fun provideImagesPath(context: MainApplication): File {
        return File(context.filesDir, "controller").apply {
            mkdirs()
        }
    }

    @Module
    interface BindsModule {

        @Named("disk_bitmap_data_source")
        @Binds
        fun provideDiskBitmapDataSource(diskBitmapDataSource: DiskBitmapDataSource): BitmapDataSource

        @Named("network_bitmap_data_source")
        @Binds
        fun provideNetworkBitmapDataSource(networkBitmapDataSource: NetworkBitmapDataSource): BitmapDataSource

        @Binds
        fun provideDiskResultsDataSource(diskResultsDataSource: DiskResultsDataSource): ResultsDataSource

        @Binds
        fun provideDiskRemoveResultDataSource(diskRemoveResultSource: DiskRemoveResultSource): RemoveResultSource

        @Binds
        fun provideDiskSetControllerImageSource(diskSetControllerImageSource: DiskSetControllerImageSource): SetControllerImageSource
    }
}