package net.kelmer.correostracker.util.ext

import android.widget.TextView

fun TextView.textOrElse(originalText: String, orElse: String) {
    if (!originalText.isNullOrEmpty())
        this.text = originalText
    else {
        this.text = orElse
    }
}
