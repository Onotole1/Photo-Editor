package com.spitchenko.photoeditor.feature.main.presentation.dagger

import android.graphics.Bitmap
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.spitchenko.photoeditor.R
import com.spitchenko.photoeditor.databinding.ActivityMainBinding
import com.spitchenko.photoeditor.feature.main.domain.entity.BitmapWithId
import com.spitchenko.photoeditor.feature.main.domain.entity.SetImageRequest
import com.spitchenko.photoeditor.feature.main.domain.entity.UriWithId
import com.spitchenko.photoeditor.feature.main.presentation.view.MainActivity
import com.spitchenko.photoeditor.feature.main.presentation.view.adapter.TableDecoration
import com.spitchenko.photoeditor.feature.main.presentation.viewmodel.MainViewModel
import com.spitchenko.domain.model.State
import com.spitchenko.domain.usecase.UseCase
import com.spitchenko.domain.usecase.UseCaseCompletable
import com.spitchenko.domain.usecase.UseCaseSingle
import com.spitchenko.presentation.view.binding.adapter.BinderAdapter
import com.spitchenko.presentation.view.binding.adapter.BindingViewHolder
import com.spitchenko.presentation.viewmodel.ViewModelFactory
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
        setControllerImage: UseCaseCompletable<SetImageRequest>,
        getExif: UseCaseSingle<Map<String, String>, Unit>,
        getResults: UseCaseSingle<List<BitmapWithId>, Unit>
    ): MainViewModel = ViewModelFactory {
        MainViewModel(
            getBitmapFromUri,
            rotateBitmap,
            mirrorBitmap,
            invertBitmap,
            removeResult,
            setControllerImage,
            getExif,
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