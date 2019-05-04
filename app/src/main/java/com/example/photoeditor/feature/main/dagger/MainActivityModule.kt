package com.example.photoeditor.feature.main.dagger

import android.graphics.Bitmap
import android.net.Uri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.photoeditor.R
import com.example.photoeditor.databinding.ActivityMainBinding
import com.example.photoeditor.feature.main.presentation.view.MainActivity
import com.example.photoeditor.feature.main.presentation.view.adapter.TableDecoration
import com.example.photoeditor.feature.main.presentation.viewmodel.MainViewModel
import com.example.photoeditor.shared.domain.model.State
import com.example.photoeditor.shared.domain.usecase.UseCase
import com.example.photoeditor.shared.presentation.viewmodel.EventsDispatcher
import com.example.photoeditor.shared.presentation.viewmodel.ViewModelFactory
import com.example.photoeditor.utils.databinding.adapter.BinderAdapter
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class MainActivityModule {

    @Provides
    fun provideViewModel(
        context: MainActivity,
        getBitmapFromUri: UseCase<Bitmap, Uri>,
        @Named("rotate_bitmap")
        rotateBitmap: UseCase<State<Bitmap>, Bitmap>,
        @Named("invert_bitmap")
        invertBitmap: UseCase<State<Bitmap>, Bitmap>,
        @Named("mirror_bitmap")
        mirrorBitmap: UseCase<State<Bitmap>, Bitmap>,
        getBitmapFromUrl: UseCase<State<Bitmap>, String>
    ): MainViewModel = ViewModelFactory {
        MainViewModel(EventsDispatcher(), getBitmapFromUri, rotateBitmap, mirrorBitmap, invertBitmap, getBitmapFromUrl)
    }.let {
        ViewModelProvider(context, it)[MainViewModel::class.java]
    }

    @Provides
    fun provideBinding(context: MainActivity): ActivityMainBinding {
        return DataBindingUtil.setContentView<ActivityMainBinding>(context, R.layout.activity_main).apply {
            activityMainRecyclerView.apply {
                adapter = BinderAdapter(context)
                addItemDecoration(TableDecoration(context))
            }
        }
    }
}