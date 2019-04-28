package com.example.photoeditor.feature.main.presentation.view

import com.example.photoeditor.BR
import com.example.photoeditor.R
import com.example.photoeditor.databinding.ActivityMainBinding
import com.example.photoeditor.feature.main.presentation.viewmodel.MainViewModel
import com.example.photoeditor.shared.presentation.view.activity.BaseEventsActivity
import com.example.photoeditor.shared.presentation.view.dialog.AlertDialogFragment
import javax.inject.Inject

class MainActivity : BaseEventsActivity<ActivityMainBinding, MainViewModel, MainViewModel.EventsListener>(),
    MainViewModel.EventsListener,
    AlertDialogFragment.OnListItemClickListener {

    override val eventsListener: MainViewModel.EventsListener = this

    override val viewModelVariableId: Int = BR.viewModel

    @Inject
    override lateinit var viewModel: MainViewModel

    @Inject
    override lateinit var binding: ActivityMainBinding

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
