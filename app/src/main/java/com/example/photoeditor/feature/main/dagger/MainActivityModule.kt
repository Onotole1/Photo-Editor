package com.example.photoeditor.feature.main.dagger

import android.graphics.Bitmap
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.example.photoeditor.R
import com.example.photoeditor.databinding.ActivityMainBinding
import com.example.photoeditor.feature.main.domain.entity.BitmapWithId
import com.example.photoeditor.feature.main.domain.entity.SetImageRequest
import com.example.photoeditor.feature.main.domain.entity.UriWithId
import com.example.photoeditor.feature.main.presentation.view.MainActivity
import com.example.photoeditor.feature.main.presentation.view.adapter.TableDecoration
import com.example.photoeditor.feature.main.presentation.viewmodel.MainViewModel
import com.example.photoeditor.shared.domain.model.State
import com.example.photoeditor.shared.domain.usecase.UseCase
import com.example.photoeditor.shared.domain.usecase.UseCaseCompletable
import com.example.photoeditor.shared.presentation.viewmodel.EventsDispatcher
import com.example.photoeditor.shared.presentation.viewmodel.ViewModelFactory
import com.example.photoeditor.utils.databinding.adapter.BinderAdapter
import com.example.photoeditor.utils.databinding.adapter.BindingViewHolder
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class MainActivityModule {

    @Provides
    fun provideViewModel(
        context: MainActivity,
        getBitmapFromUri: UseCase<State<Bitmap>, UriWithId>,
        @Named("rotate_bitmap")
        rotateBitmap: UseCase<State<Bitmap>, BitmapWithId>,
        @Named("invert_bitmap")
        invertBitmap: UseCase<State<Bitmap>, BitmapWithId>,
        @Named("mirror_bitmap")
        mirrorBitmap: UseCase<State<Bitmap>, BitmapWithId>,
        removeResult: UseCaseCompletable<Long>,
        getResults: UseCase<List<BitmapWithId>, Unit>,
        setControllerImage: UseCaseCompletable<SetImageRequest>
    ): MainViewModel = ViewModelFactory {
        MainViewModel(
            EventsDispatcher(),
            getBitmapFromUri,
            rotateBitmap,
            mirrorBitmap,
            invertBitmap,
            removeResult,
            setControllerImage,
            getResults
        )
    }.let {
        ViewModelProvider(context, it)[MainViewModel::class.java]
    }

    @Provides
    fun provideBinding(context: MainActivity, viewModel: MainViewModel): ActivityMainBinding {
        return DataBindingUtil.setContentView<ActivityMainBinding>(context, R.layout.activity_main).apply {
            activityMainRecyclerView.apply {
                adapter = createAdapter(context, viewModel)

                addItemDecoration(TableDecoration(context))
            }
        }
    }

    private fun createAdapter(context: MainActivity, viewModel: MainViewModel): BinderAdapter =
        object : BinderAdapter(context) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<ViewDataBinding> {

                return super.onCreateViewHolder(parent, viewType).also { binding ->

                    if (viewType == R.layout.item_result) {
                        binding.itemView.setOnClickListener {
                            viewModel.onImageClick(binding.adapterPosition)
                        }
                    }
                }
            }
        }
}