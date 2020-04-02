package net.kelmer.correostracker.customviews

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import androidx.annotation.StringRes


object ConfirmDialog {

    fun confirmDialog(context: Context,
                      @StringRes title: Int,
                      @StringRes message: Int,
                      okAction: () -> Unit) {

        AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(context.getString(android.R.string.ok)) { i, _ ->
                    okAction()
                    i.dismiss()
                }
                .setNegativeButton(context.getString(android.R.string.cancel)) { i, _ ->
                    i.dismiss()
                }
                .show()
    }
}

