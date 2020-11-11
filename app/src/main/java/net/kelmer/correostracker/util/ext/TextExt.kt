package net.kelmer.correostracker.util.ext

import android.widget.TextView
import net.kelmer.correostracker.R

fun TextView.textOrElse(originalText: String, orElse: String) {
    if (!originalText.isNullOrEmpty())
        this.text = originalText
    else {
        this.text = orElse
    }
}

fun TextView.dimen(largo: String, ancho: String, alto: String, orElse: String) {
    if (!largo.isNullOrEmpty() && !ancho.isNullOrEmpty() && !alto.isNullOrEmpty()) {
        textOrElse(context.getString(R.string.dimensiones_placeholder, largo, ancho, alto), orElse)
    } else {
        this.text = orElse
    }
}

fun TextView.peso(peso: String, orElse: String) {
    if (!peso.isNullOrEmpty()) {
        textOrElse(context.getString(R.string.peso_placeholder, peso), orElse)
    } else {
        this.text = orElse
    }
}
