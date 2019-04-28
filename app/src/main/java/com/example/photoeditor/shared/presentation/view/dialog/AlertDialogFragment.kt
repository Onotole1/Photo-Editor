package com.example.photoeditor.shared.presentation.view.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.photoeditor.shared.presentation.view.fragment.getArgs
import com.example.photoeditor.shared.presentation.view.fragment.putArgs
import java.lang.NullPointerException

class AlertDialogFragment : DialogFragment(), DialogInterface.OnClickListener {

    private var onDialogButtonPressedListener: OnDialogButtonPressedListener? = null
    private var onListItemClickListener: OnListItemClickListener? = null

    private lateinit var args: Arguments

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val receiver = targetFragment ?: parentFragment ?: context

        onDialogButtonPressedListener = receiver as? OnDialogButtonPressedListener
        onListItemClickListener = receiver as? OnListItemClickListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        args = arguments?.getArgs() ?: throw NullPointerException("Args must be set!")

        return AlertDialog.Builder(requireContext()).apply {

            args.positiveButton?.also {
                setPositiveButton(it, this@AlertDialogFragment)
            }

            args.negativeButton?.also {
                setNegativeButton(it, this@AlertDialogFragment)
            }

            args.singleChoiceItemsRes?.also {
                setItems(it, this@AlertDialogFragment)
            }

            args.messageRes?.also {
                setMessage(it)
            }
        }.create()
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        if (which < 0) {
            onDialogButtonPressedListener?.onDialogButtonPressed(args.dialogTag, this, which)
        } else {
            onListItemClickListener?.onListItemClick(args.dialogTag, this, which)
        }
    }

    interface OnDialogButtonPressedListener {
        fun onDialogButtonPressed(dialogTag: String, dialog: AlertDialogFragment, buttonId: Int)
    }

    interface OnListItemClickListener {
        fun onListItemClick(dialogTag: String, dialog: AlertDialogFragment, position: Int)
    }

    data class Arguments(val dialogTag: String) : Parcelable {

        var positiveButton: Int? = null
        var negativeButton: Int? = null
        var singleChoiceItemsRes: Int? = null
        var messageRes: Int? = null

        constructor(parcel: Parcel) : this(parcel.readString()!!) {
            positiveButton = parcel.readValue(Int::class.java.classLoader) as? Int
            negativeButton = parcel.readValue(Int::class.java.classLoader) as? Int
            singleChoiceItemsRes = parcel.readValue(Int::class.java.classLoader) as? Int
            messageRes = parcel.readValue(Int::class.java.classLoader) as? Int
        }

        fun message(@StringRes messageRes: Int) = apply { this.messageRes = messageRes }
        fun positiveButton(@StringRes positiveButton: Int) = apply { this.positiveButton = positiveButton }
        fun negativeButton(@StringRes negativeButton: Int) = apply { this.negativeButton = negativeButton }
        fun singleChoiceItemsRes(@ArrayRes singleChoiceItemsRes: Int) =
            apply { this.singleChoiceItemsRes = singleChoiceItemsRes }

        fun build() = AlertDialogFragment().apply {
            putArgs(this@Arguments)
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(dialogTag)
            parcel.writeValue(positiveButton)
            parcel.writeValue(negativeButton)
            parcel.writeValue(singleChoiceItemsRes)
            parcel.writeValue(messageRes)
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

fun AppCompatActivity.showAlert(tag: String, block: AlertDialogFragment.Arguments.() -> Unit) {
    AlertDialogFragment.Arguments(tag)
        .also(block)
        .build()
        .show(supportFragmentManager, tag)
}