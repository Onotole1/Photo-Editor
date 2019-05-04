package com.example.photoeditor.shared.presentation.view.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.photoeditor.shared.presentation.view.fragment.getArgs
import com.example.photoeditor.shared.presentation.view.fragment.putArgs

open class AlertDialogFragment : DialogFragment(), DialogInterface.OnClickListener {

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
        args = getArgs()

        return AlertDialog.Builder(requireContext()).apply {
            setupDialog(this, savedInstanceState)
        }.create()
    }

    open fun getArgs(): Arguments {
        return arguments?.getArgs() ?: throw NullPointerException("Args must be set!")
    }

    open fun setupDialog(builder: AlertDialog.Builder, savedInstanceState: Bundle?) {
        args.positiveButton?.also {
            builder.setPositiveButton(it, this@AlertDialogFragment)
        }

        args.negativeButton?.also {
            builder.setNegativeButton(it, this@AlertDialogFragment)
        }

        args.singleChoiceItemsRes?.also {
            builder.setItems(it, this@AlertDialogFragment)
        }

        args.messageRes?.also {
            builder.setMessage(it)
        }
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        if (which < 0) {
            onDialogButtonPressedListener?.onDialogButtonPressed(args.dialogTag, this, which)
            onDialogButtonPressed(which)
        } else {
            onListItemClickListener?.onListItemClick(args.dialogTag, this, which)
        }
    }

    open fun onDialogButtonPressed(which: Int) {}

    interface OnDialogButtonPressedListener {
        fun onDialogButtonPressed(dialogTag: String, dialog: AlertDialogFragment, buttonId: Int)
    }

    interface OnListItemClickListener {
        fun onListItemClick(dialogTag: String, dialog: AlertDialogFragment, position: Int)
    }

    open class Arguments(val dialogTag: String) : Parcelable {

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

        open fun build() = AlertDialogFragment().apply {
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