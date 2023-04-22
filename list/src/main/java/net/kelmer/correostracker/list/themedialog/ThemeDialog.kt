package net.kelmer.correostracker.list.themedialog

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AlertDialog
import net.kelmer.correostracker.list.R

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
    val themes = ThemeMode.values().map {
        context.getString(it.stringRes)
    }.toTypedArray()
    builder.setItems(themes) { _, which ->
        listener(ThemeMode.fromPosition(which))
    }

    // create and show the alert dialog
    return builder.create()
}
