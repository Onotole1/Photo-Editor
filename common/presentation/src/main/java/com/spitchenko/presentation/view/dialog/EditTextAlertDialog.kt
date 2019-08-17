package com.spitchenko.presentation.view.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcelable
import android.view.WindowManager
import android.widget.EditText
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.spitchenko.presentation.view.arguments.getArgs
import com.spitchenko.presentation.view.arguments.putArgs
import kotlinx.android.parcel.Parcelize


open class EditTextAlertDialog : AlertDialogFragment() {
    private lateinit var editText: EditText

    private lateinit var args: EditTextArguments

    override fun setupDialog(builder: AlertDialog.Builder, savedInstanceState: Bundle?) {
        super.setupDialog(builder, savedInstanceState)

        editText = EditText(requireContext()).apply {
            args.editTextHintRes?.also {
                hint = getString(it)
            }

            onRestoreInstanceState(savedInstanceState?.getArgs())

            requestFocus()
        }

        builder.setView(editText)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        super.onCreateDialog(savedInstanceState).also {

            it.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        }

    override fun getArgs(): Arguments =
        (getArgs<EditTextArguments>() ?: throw NullPointerException("Args must be set!")).let {
            args = it
            it.alertArguments
        }

    override fun onDialogButtonPressed(which: Int) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            ((targetFragment ?: parentFragment ?: context) as? EditTextDialogListener)?.onTextSelected(
                editText.text.toString()
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        editText.onSaveInstanceState()?.also {
            outState.putArgs(it)
        }
    }

    interface EditTextDialogListener {
        fun onTextSelected(text: String)
    }

    @Parcelize
    data class EditTextArguments(
        @StringRes val editTextHintRes: Int?,
        val alertArguments: Arguments
    ) : Parcelable

    class EditTextAlertBuilder(dialogTag: String) : AlertBuilder(dialogTag) {
        var editTextHintRes: Int? = null

        private fun buildEditTextArgs() = EditTextArguments(editTextHintRes, buildAlertArgs())

        override fun build(): DialogFragment {
            return EditTextAlertDialog().apply {
                putArgs(buildEditTextArgs())
            }
        }
    }
}

inline fun AppCompatActivity.showEditTextAlert(
    tag: String,
    block: EditTextAlertDialog.EditTextAlertBuilder.() -> Unit
) = EditTextAlertDialog.EditTextAlertBuilder(tag)
    .also(block)
    .build()
    .show(supportFragmentManager, tag)
