package com.spitchenko.presentation.view.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.spitchenko.presentation.view.fragment.getArgs
import com.spitchenko.presentation.view.fragment.putArgs
import kotlinx.android.parcel.Parcelize

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

        args.message?.also {
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

    @Parcelize
    data class Arguments(
        val dialogTag: String,
        val positiveButton: Int? = null,
        val negativeButton: Int? = null,
        val singleChoiceItemsRes: Int? = null,
        val messageRes: Int? = null,
        val message: String? = null
    ) : Parcelable

    open class AlertBuilder(
        private val dialogTag: String
    ) {
        var positiveButton: Int? = null
        var negativeButton: Int? = null
        var singleChoiceItemsRes: Int? = null
        var messageRes: Int? = null
        var message: String? = null

        protected fun buildAlertArgs() =
            Arguments(dialogTag, positiveButton, negativeButton, singleChoiceItemsRes, messageRes, message)

        open fun build(): DialogFragment {
            return AlertDialogFragment().apply {
                putArgs(buildAlertArgs())
            }
        }
    }
}

fun AppCompatActivity.showAlert(tag: String, block: AlertDialogFragment.AlertBuilder.() -> Unit) {
    AlertDialogFragment.AlertBuilder(tag)
        .also(block)
        .build()
        .show(supportFragmentManager, tag)
}