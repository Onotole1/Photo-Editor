package com.spitchenko.photoeditor.feature.main.presentation.dagger

import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.databinding.ViewDataBinding
import com.spitchenko.photoeditor.R
import com.spitchenko.photoeditor.databinding.ActivityMainBinding
import com.spitchenko.photoeditor.feature.main.domain.usecase.getbitmapfromuri.GetBitmapFromUri
import com.spitchenko.photoeditor.feature.main.domain.usecase.getexif.GetExif
import com.spitchenko.photoeditor.feature.main.domain.usecase.getresults.GetResults
import com.spitchenko.photoeditor.feature.main.domain.usecase.removeresult.RemoveResult
import com.spitchenko.photoeditor.feature.main.domain.usecase.setcontrollerimage.SetControllerImage
import com.spitchenko.photoeditor.feature.main.domain.usecase.transform.invertbitmap.InvertBitmap
import com.spitchenko.photoeditor.feature.main.domain.usecase.transform.mirrorbitmap.MirrorBitmap
import com.spitchenko.photoeditor.feature.main.domain.usecase.transform.rotatebitmap.RotateBitmap
import com.spitchenko.photoeditor.feature.main.presentation.view.MainActivity
import com.spitchenko.photoeditor.feature.main.presentation.view.adapter.TableDecoration
import com.spitchenko.photoeditor.feature.main.presentation.viewmodel.MainViewModel
import com.spitchenko.photoeditor.feature.main.presentation.viewmodel.MainViewModelUseCases
import com.spitchenko.presentation.view.activity.getViewModel
import com.spitchenko.presentation.view.binding.adapter.BinderAdapter
import com.spitchenko.presentation.view.binding.adapter.BindingViewHolder
import com.spitchenko.presentation.viewmodel.EventsDispatcher
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {

    @Provides
    fun provideViewModel(
        context: MainActivity,
        getBitmapFromUri: GetBitmapFromUri,
        rotateBitmap: RotateBitmap,
        invertBitmap: InvertBitmap,
        mirrorBitmap: MirrorBitmap,
        removeResult: RemoveResult,
        setControllerImage: SetControllerImage,
        getExif: GetExif,
        getResults: GetResults
    ): MainViewModel {

        return context.getViewModel {
            MainViewModel(
                MainViewModelUseCases(
                    getBitmapFromUri,
                    rotateBitmap,
                    mirrorBitmap,
                    invertBitmap,
                    removeResult,
                    setControllerImage,
                    getExif,
                    getResults
                ),

                EventsDispatcher(),

                ObservableArrayList()
            )
        }.also {
            it.init()

            createBinding(context, it)
        }
    }

    private fun createBinding(
        context: MainActivity,
        viewModel: MainViewModel
    ): ActivityMainBinding {
        return DataBindingUtil.setContentView<ActivityMainBinding>(context, R.layout.activity_main).apply {
            activityMainRecyclerView.apply {
                adapter = createAdapter(context, viewModel)

                addItemDecoration(TableDecoration(context))
            }

            viewModel.eventsDispatcher.bind(context, context)

            bindingList = viewModel.bindingList
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