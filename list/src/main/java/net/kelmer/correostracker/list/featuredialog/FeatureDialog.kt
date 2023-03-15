package net.kelmer.correostracker.list.featuredialog

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import net.kelmer.correostracker.list.R

interface FeatureBlurbListener {
    fun ok()
    fun cancel()
    fun kofi()
}

fun featureBlurbDialog(
        context: Context,
        @StringRes titleText: Int,
        @StringRes okText: Int,
        okListener: () -> Unit,
        githubListener: () -> Unit
): androidx.appcompat.app.AlertDialog {

    return featureBlurbDialog(
            context = context,
            titleText = context.getString(titleText),
            okText = context.getString(okText),
            okListener = okListener,
            kofiListener = {},
            githubListener = githubListener
    )
}

@SuppressLint("InflateParams")
fun featureBlurbDialog(
        context: Context,
        titleText: String,
        okText: String,
        okListener: () -> Unit,
        kofiListener: () -> Unit,
        githubListener: () -> Unit
): androidx.appcompat.app.AlertDialog {

    val layoutInflater = LayoutInflater.from(context)
    val inflate = layoutInflater.inflate(R.layout.layout_newfeatures, null)

    val builder = androidx.appcompat.app.AlertDialog.Builder(context)
    builder.setView(inflate)
    val create = builder.create()
    val title = inflate.findViewById<AppCompatTextView>(R.id.dialog_title)
    val featureList = inflate.findViewById<RecyclerView>(R.id.dialog_list)
    val ok = inflate.findViewById<Button>(R.id.dialog_yes_button)
    val github = inflate.findViewById<ImageView>(R.id.github_button)
    val cancel = inflate.findViewById<AppCompatImageButton>(R.id.dialog_no_button)
    ok?.text = okText
    title?.text = titleText

    val groupAdapter = GroupAdapter<GroupieViewHolder>()
    featureList.adapter = groupAdapter
    groupAdapter.update(listOf(
        Feature("2.2.7", R.string.changes_2_2_7),
        Feature("2.2.6", R.string.changes_2_2_6),
        Feature("2.1.0", R.string.changes_2_1_0),
        Feature("2.0.0", R.string.changes_2_0_0),
        Feature("1.9.5", R.string.changes_1_9_5),
        Feature("1.9.0", R.string.changes_1_9_0),
        Feature("1.8.0", R.string.changes_1_8_0),
        Feature("1.7.2", R.string.changes_1_7_2),
        Feature("1.7.0", R.string.changes_1_7_0),
    ).map { FeatureListItem(it) })

    github.setOnClickListener {
        githubListener()
    }

    ok?.setOnClickListener {
        okListener()
        create.dismiss()
    }
    cancel?.setOnClickListener {
        create.dismiss()
    }
    return create
}
