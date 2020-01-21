package net.kelmer.correostracker.ui.featuredialog


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import net.kelmer.correostracker.R

interface FeatureBlurbListener {
    fun ok()
    fun cancel()
}

fun featureBlurbDialog(
        context: Context,
        @StringRes titleText: Int,
        @StringRes contentText: Int,
        @StringRes okText: Int,
        okListener: () -> Unit,
        cancelListener: () -> Unit
): androidx.appcompat.app.AlertDialog {

    return featureBlurbDialog(context, context.getString(titleText), context.getString(contentText), context.getString(okText), okListener, cancelListener)
}

@SuppressLint("InflateParams")
fun featureBlurbDialog(
        context: Context,
        titleText: String,
        contentText: String,
        okText: String,
        okListener: () -> Unit,
        cancelListener: () -> Unit
): androidx.appcompat.app.AlertDialog {

    val layoutInflater = LayoutInflater.from(context)
    val inflate = layoutInflater.inflate(R.layout.layout_newfeatures, null)

    val builder = androidx.appcompat.app.AlertDialog.Builder(context)
    builder.setView(inflate)
    val create = builder.create()
    val title = inflate.findViewById<AppCompatTextView>(R.id.dialog_title)
    val text = inflate.findViewById<AppCompatTextView>(R.id.dialog_text)
    val ok = inflate.findViewById<Button>(R.id.dialog_yes_button)
    val cancel = inflate.findViewById<AppCompatImageButton>(R.id.dialog_no_button)

    ok?.text = okText
    title?.text = titleText
    text?.text = contentText
    ok?.setOnClickListener {
        okListener()
        create.dismiss()
    }
    cancel?.setOnClickListener {
        cancelListener()
        create.dismiss()
    }
    return create
}
