package net.kelmer.correostracker.ui.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.kelmer.correostracker.R
import net.kelmer.correostracker.data.model.dto.ParcelDetailStatus
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.databinding.RvParcelItemBinding
import net.kelmer.correostracker.ext.isVisible
import java.text.SimpleDateFormat
import java.util.Date


/**z
 * Created by gabriel on 25/03/2018.
 */
class ParcelListAdapter constructor(
        private val clickListener: ParcelClickListener
) : RecyclerView.Adapter<ParcelListAdapter.ParcelListViewHolder>() {


    private var allItems = mutableListOf<LocalParcelReference>()
    private var filteredItems = mutableListOf<LocalParcelReference>()


    override fun getItemCount(): Int =
            filteredItems.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParcelListViewHolder {
        return ParcelListViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ParcelListViewHolder, position: Int) {
        holder.bind(filteredItems[position], clickListener)
    }


    class ParcelListViewHolder(private val binding: RvParcelItemBinding) : RecyclerView.ViewHolder(binding.root) {

        private val dateFormat = SimpleDateFormat("dd/MM/yyy HH:mm:ss")

        companion object {
            fun create(parent: ViewGroup) = ParcelListViewHolder(RvParcelItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

        fun bind(parcel: LocalParcelReference,
                 clickListener: ParcelClickListener) = with(itemView) {

            binding.parcelName.text = parcel.parcelName
            binding.parcelCode.text = parcel.trackingCode

            binding.ultimoEstado.text = parcel.ultimoEstado?.buildUltimoEstado()
                    ?: context.getString(R.string.status_unknown)

            when (parcel.stance) {
                LocalParcelReference.Stance.INCOMING -> {
                    binding.parcelStance.setText(R.string.incoming)
                }
                LocalParcelReference.Stance.OUTGOING -> {
                    binding.parcelStance.setText(R.string.outgoing)
                }
            }

            binding.parcelCardview.setOnClickListener {
                clickListener.click(parcel)
            }
            binding.more.setOnClickListener {
                clickListener.dots(binding.more, parcel)
            }

            binding.parcelCardview.setOnLongClickListener {
                clickListener.longPress(parcel)
                true
            }

            binding.parcelProgress.isVisible = parcel.isLoading
            binding.parcelStatus.isVisible = !parcel.isLoading

            val faseNumber = parcel.ultimoEstado?.fase?.toInt()
            val fase = if (faseNumber != null) ParcelDetailStatus.Fase.fromFase(faseNumber) else ParcelDetailStatus.Fase.OTHER

            binding.parcelStatus.setImageResource(fase.drawable)

            var lastChecked = parcel.lastChecked

            if (lastChecked != null && lastChecked > 0) {
                //                val relativeText = DateUtils.getRelativeTimeSpanString(lastChecked, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
                //                last_checked.text = context.getString(R.string.lastchecked, relativeText)
                binding.lastChecked.text = context.getString(R.string.lastchecked, dateFormat.format(Date(lastChecked)))

            }
            binding.lastChecked.isVisible = lastChecked != null && lastChecked > 0
        }

    }

    fun filter(text: String) {
        filteredItems = if (text.isNullOrEmpty()) {
            allItems
        } else {
            allItems.filter {
                it.parcelName.contains(text, true) || it.trackingCode.contains(text, true)
            }.toMutableList()
        }
        notifyDataSetChanged()
    }

    fun updateItems(data: List<LocalParcelReference>) {
        allItems = data.toMutableList()
        notifyDataSetChanged()
    }

    fun setLoading(code: String, loading: Boolean) {
        val filter = allItems.filter { it.trackingCode == code }
        if (filter.isNotEmpty()) {
            filter.first().isLoading = loading
            notifyItemChanged(allItems.indexOf(filter.first()))
        }
    }

    fun getAllItems(): List<LocalParcelReference> = allItems
}