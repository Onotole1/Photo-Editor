package com.example.photoeditor.feature.main.dagger

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.photoeditor.R
import com.example.photoeditor.databinding.ActivityMainBinding
import com.example.photoeditor.feature.main.presentation.view.MainActivity
import com.example.photoeditor.feature.main.presentation.view.adapter.TableDecoration
import com.example.photoeditor.feature.main.presentation.viewmodel.MainViewModel
import com.example.photoeditor.shared.presentation.viewmodel.EventsDispatcher
import com.example.photoeditor.shared.presentation.viewmodel.ViewModelFactory
import com.example.photoeditor.utils.databinding.adapter.BinderAdapter
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {

    @Provides
    fun provideViewModel(context: MainActivity): MainViewModel = ViewModelFactory {
        MainViewModel(EventsDispatcher())
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