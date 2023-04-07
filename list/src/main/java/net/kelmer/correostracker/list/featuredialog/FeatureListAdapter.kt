package net.kelmer.correostracker.list.featuredialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import net.kelmer.correostracker.list.databinding.RvFeatureBinding

class FeatureListAdapter : RecyclerView.Adapter<FeatureListAdapter.FeatureListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureListViewHolder {
        return FeatureListViewHolder.create(parent)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: FeatureListViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun setList(list: List<Feature>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    private val list: MutableList<Feature> = mutableListOf()

    class FeatureListViewHolder(private val binding: RvFeatureBinding) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun create(parent: ViewGroup): FeatureListViewHolder {
                return FeatureListViewHolder(RvFeatureBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
        }

        fun bind(feature: Feature) = with(itemView) {
            binding.featureText.text = itemView.context.getString(feature.text)
            binding.featureVersion.text = feature.version
        }
    }

}
