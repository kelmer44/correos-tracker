package net.kelmer.correostracker.ui.themedialog

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AlertDialog
import net.kelmer.correostracker.R
import net.kelmer.correostracker.data.prefs.ThemeMode


/**
 * Created by Gabriel Sanmartín on 11/09/2020.
 */

@SuppressLint("InflateParams")
fun themeSelectionDialog(
        context: Context,
        listener: (ThemeMode) -> Unit
): AlertDialog {
    // setup the alert builder
    // setup the alert builder
    val builder = AlertDialog.Builder(context)
    builder.setTitle(R.string.theme_title)

    // add a list
    val animals = ThemeMode.values().map {
        context.getString(it.stringRes)
    }.toTypedArray()
    builder.setItems(animals) { dialog, which ->
        listener(ThemeMode.fromPosition(which))
    }

    // create and show the alert dialog
    return builder.create()
}