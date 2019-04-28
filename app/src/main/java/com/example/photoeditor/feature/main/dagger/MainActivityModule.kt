package com.example.photoeditor.feature.main.dagger

import androidx.recyclerview.widget.RecyclerView
import com.example.photoeditor.feature.main.presentation.view.MainActivity
import com.example.photoeditor.feature.main.presentation.view.adapter.TableDecoration
import com.example.photoeditor.utils.databinding.adapter.BinderAdapter
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {

    @Provides
    fun provideAdapter(context: MainActivity): RecyclerView.Adapter<*> = BinderAdapter(context)

    @Provides
    fun provideTableDecoration(context: MainActivity): RecyclerView.ItemDecoration = TableDecoration(context)
}