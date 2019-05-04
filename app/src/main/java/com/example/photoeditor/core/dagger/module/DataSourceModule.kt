package com.example.photoeditor.core.dagger.module

import com.example.photoeditor.core.MainApplication
import com.example.photoeditor.feature.main.data.repository.getbitmapfromurl.datasource.BitmapDataSource
import com.example.photoeditor.feature.main.data.repository.getbitmapfromurl.datasource.DiskBitmapDataSource
import com.example.photoeditor.feature.main.data.repository.getbitmapfromurl.datasource.NetworkBitmapDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [DataSourceModule.BindsModule::class])
class DataSourceModule {

    @Named("images_path")
    @Provides
    fun provideImagesPath(context: MainApplication): String {
        return context.filesDir.absolutePath
    }

    @Module
    interface BindsModule {

        @Named("disk_bitmap_data_source")
        @Binds
        fun provideDiskBitmapDataSource(diskBitmapDataSource: DiskBitmapDataSource): BitmapDataSource

        @Named("network_bitmap_data_source")
        @Binds
        fun provideNetworkBitmapDataSource(networkBitmapDataSource: NetworkBitmapDataSource): BitmapDataSource
    }
}