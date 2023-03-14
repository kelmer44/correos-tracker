package net.kelmer.correostracker.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import net.kelmer.correostracker.core.R

/**
 * Created by Gabriel Sanmartín on 03/11/2020.
 */
fun Context.copyToClipboard(text: String) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("ParcelCode", text)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(this, getString(R.string.clipboard_copied), Toast.LENGTH_LONG).show()
}
