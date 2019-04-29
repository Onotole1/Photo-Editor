package com.example.photoeditor.feature.main.presentation.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.photoeditor.BR
import com.example.photoeditor.R
import com.example.photoeditor.databinding.ActivityMainBinding
import com.example.photoeditor.feature.main.presentation.viewmodel.MainViewModel
import com.example.photoeditor.shared.presentation.view.activity.BaseEventsActivity
import com.example.photoeditor.shared.presentation.view.dialog.AlertDialogFragment
import com.example.photoeditor.shared.presentation.view.dialog.showAlert
import com.example.photoeditor.utils.isPermissionGranted
import com.example.photoeditor.utils.requestPermissions
import com.example.photoeditor.utils.toast
import java.io.File
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

    private val mPhotoPicUri by lazy(LazyThreadSafetyMode.NONE) {
        FileProvider.getUriForFile(this, packageName, createPhotoFile())
    }

    override fun showImagePickerDialog() {
        showAlert(PICKER_IMAGE_DIALOG) {
            negativeButton(android.R.string.cancel)
            singleChoiceItemsRes(R.array.picker_dialog)
        }
    }

    override fun onListItemClick(dialogTag: String, dialog: AlertDialogFragment, position: Int) {
        if (dialogTag == PICKER_IMAGE_DIALOG) {
            when (Buttons.values()[position]) {

                Buttons.CAMERA_BUTTON -> {
                    if (isPermissionGranted(Manifest.permission.CAMERA)) {
                        startCamera()
                    } else {
                        requestPermissions(CAMERA_PERMISSION_REQUEST_CODE, Manifest.permission.CAMERA)
                    }
                }

                Buttons.GALLERY_BUTTON -> {
                    if (isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        startGallery()
                    } else {
                        requestPermissions(
                            READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    }
                }

                Buttons.NET_BUTTON -> {

                }
            }
        }
    }

    private fun startCamera() {
        startPicker(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
    }

    private fun startGallery() {
        startPicker(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
    }

    private fun startPicker(intent: Intent) {
        intent.apply {
            putExtra(MediaStore.EXTRA_OUTPUT, mPhotoPicUri)
        }.also {
            startActivityForResult(it, IMAGE_PICKER_REQUEST_CODE)
        }
    }

    private fun createPhotoFile() = File(
            getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            DEFAULT_FILE_NAME
        )

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val permissionsResults = permissions.mapIndexed { index, permission ->
            permission to grantResults[index]
        }.toMap()

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            checkPermissionResult(
                permissionsResults,
                Manifest.permission.CAMERA,
                R.string.activity_main_camera_permission_explanation
            ) {
                startCamera()
            }
        } else if (requestCode == READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE) {
            checkPermissionResult(
                permissionsResults,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                R.string.activity_main_gallery_permission_explanation
            ) {
                startGallery()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_PICKER_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            viewModel.setImage(data?.data ?: mPhotoPicUri)
        }
    }

    private fun checkPermissionResult(
        permissionsResults: Map<String, Int>,
        permission: String,
        @StringRes explanation: Int,
        onSuccess: () -> Unit
    ) {
        if (permissionsResults[permission] == PackageManager.PERMISSION_GRANTED) {
            onSuccess()
        } else {
            toast(explanation)
        }
    }

    private enum class Buttons {
        CAMERA_BUTTON,
        GALLERY_BUTTON,
        NET_BUTTON,
    }

    private companion object {
        const val PICKER_IMAGE_DIALOG = "com.example.photoeditor.feature.main.presentation.view.PICKER_IMAGE_DIALOG"

        const val CAMERA_PERMISSION_REQUEST_CODE = 483

        const val READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 274

        const val IMAGE_PICKER_REQUEST_CODE = 943

        const val DEFAULT_FILE_NAME = "image.png"
    }
}