package com.example.photoeditor.feature.main.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.photoeditor.R
import com.example.photoeditor.databinding.ActivityMainBinding
import com.example.photoeditor.feature.main.presentation.view.adapter.TableDecoration
import com.example.photoeditor.feature.main.presentation.viewmodel.MainViewModel
import com.example.photoeditor.shared.presentation.view.dialog.AlertDialogFragment
import com.example.photoeditor.shared.presentation.viewmodel.EventsDispatcher
import com.example.photoeditor.utils.databinding.adapter.BinderAdapter

class MainActivity : AppCompatActivity(), MainViewModel.EventsListener, AlertDialogFragment.OnListItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).apply {
            activityMainRecyclerView.apply {
                adapter = BinderAdapter(this@MainActivity)
                addItemDecoration(TableDecoration(this@MainActivity))
            }
            viewModel = MainViewModel(EventsDispatcher<MainViewModel.EventsListener>().also {
                it.bind(this@MainActivity, this@MainActivity)
            })
        }
    }

    override fun showImagePickerDialog() {
        AlertDialogFragment.Arguments(PICKER_IMAGE_DIALOG)
            .negativeButton(android.R.string.cancel)
            .singleChoiceItemsRes(R.array.picker_dialog)
            .build()
            .show(supportFragmentManager, PICKER_IMAGE_DIALOG)
    }

    override fun onListItemClick(dialogTag: String, dialog: AlertDialogFragment, position: Int) {
        if (dialogTag == PICKER_IMAGE_DIALOG) {
            when (Buttons.values()[position]) {
                Buttons.CAMERA_BUTTON -> {

                }
                Buttons.GALLERY_BUTTON -> {

                }
                Buttons.NET_BUTTON -> {

                }
            }
        }
    }

    private enum class Buttons {
        CAMERA_BUTTON,
        GALLERY_BUTTON,
        NET_BUTTON,
    }

    private companion object {
        const val PICKER_IMAGE_DIALOG = "com.example.photoeditor.feature.main.presentation.view.PICKER_IMAGE_DIALOG"
    }
}
