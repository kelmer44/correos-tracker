package net.kelmer.correostracker.ui.iar

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import net.kelmer.correostracker.R
import net.kelmer.correostracker.data.prefs.ThemeMode

@SuppressLint("InflateParams")
fun inAppReviewDialog(
    context: Context,
    listener: () -> Unit
): AlertDialog {
    // setup the alert builder
    val builder = AlertDialog.Builder(context)
    builder.setTitle(R.string.inapp_title)

    builder.setMessage(R.string.inapp_message)
    builder.setPositiveButton(
        android.R.string.ok
    ) { _, _ -> listener() }

    // create and show the alert dialog
    return builder.create()
}
