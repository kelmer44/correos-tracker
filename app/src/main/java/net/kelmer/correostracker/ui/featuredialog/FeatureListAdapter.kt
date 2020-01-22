package net.kelmer.correostracker.ui.featuredialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.rv_feature.view.*
import net.kelmer.correostracker.R

class FeatureListAdapter : RecyclerView.Adapter<FeatureListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_feature, parent, false))
    }

    override fun getItemCount(): Int = list.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun setList(list: List<Feature>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    private val list: MutableList<Feature> = mutableListOf()


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(feature: Feature) = with(itemView) {
            feature_text.text = itemView.context.getString(feature.text)
            feature_version.text = feature.version
        }
    }


    data class Feature(val version: String, @StringRes val text: Int)
}