package com.example.photoeditor.shared.presentation.view.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.widget.EditText
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.photoeditor.shared.presentation.view.fragment.getArgs
import com.example.photoeditor.shared.presentation.view.fragment.putArgs


open class EditTextAlertDialog : AlertDialogFragment() {
    private lateinit var editText: EditText

    private lateinit var args: Arguments

    override fun setupDialog(builder: AlertDialog.Builder, savedInstanceState: Bundle?) {
        super.setupDialog(builder, savedInstanceState)

        editText = EditText(requireContext()).apply {
            args.editTextHintRes?.also {
                hint = getString(it)
            }

            setText(savedInstanceState?.getCharSequence(EDIT_TEXT_TEXT_STATE))
        }

        builder.setView(editText)
    }

    override fun getArgs(): AlertDialogFragment.Arguments {
        return (arguments?.getArgs<Arguments>() ?: throw NullPointerException("Args must be set!")).also {
            args = it
        }
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

        outState.putCharSequence(EDIT_TEXT_TEXT_STATE, editText.text)
    }

    private companion object {
        const val EDIT_TEXT_TEXT_STATE = "com.example.photoeditor.shared.presentation.view.dialog.EDIT_TEXT_TEXT_STATE"
    }

    interface EditTextDialogListener {
        fun onTextSelected(text: String)
    }

    class Arguments : AlertDialogFragment.Arguments {

        @StringRes
        var editTextHintRes: Int? = null

        constructor(parcel: Parcel) : super(parcel) {
            editTextHintRes = parcel.readValue(Int::class.java.classLoader) as? Int
        }

        constructor(dialogTag: String) : super(dialogTag)

        override fun build(): AlertDialogFragment {
            return EditTextAlertDialog().apply {
                putArgs(this@Arguments)
            }
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            super.writeToParcel(parcel, flags)
            parcel.writeValue(editTextHintRes)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Arguments> {
            override fun createFromParcel(parcel: Parcel): Arguments {
                return Arguments(parcel)
            }

            override fun newArray(size: Int): Array<Arguments?> {
                return arrayOfNulls(size)
            }
        }
    }
}

fun AppCompatActivity.showEditTextAlert(tag: String, block: EditTextAlertDialog.Arguments.() -> Unit) {
    EditTextAlertDialog.Arguments(tag)
        .also(block)
        .build()
        .show(supportFragmentManager, tag)
}