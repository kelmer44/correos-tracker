package net.kelmer.correostracker.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.kelmer.correostracker.list.R
import net.kelmer.correostracker.dataApi.model.dto.ParcelDetailStatus
import net.kelmer.correostracker.dataApi.model.local.LocalParcelReference
import net.kelmer.correostracker.list.databinding.RvParcelItemBinding
import net.kelmer.correostracker.util.ext.isVisible
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

        fun bind(
            parcel: LocalParcelReference,
            clickListener: ParcelClickListener
        ) = with(itemView) {

            binding.apply {

                parcelName.text = parcel.parcelName
                parcelCode.text = parcel.trackingCode

                ultimoEstado.text = parcel.ultimoEstado?.buildUltimoEstado()
                    ?: context.getString(R.string.status_unknown)

                when (parcel.stance) {
                    LocalParcelReference.Stance.INCOMING -> {
                        parcelStance.setText(R.string.incoming)
                    }
                    LocalParcelReference.Stance.OUTGOING -> {
                        parcelStance.setText(R.string.outgoing)
                    }
                }

                parcelCardview.setOnClickListener {
                    clickListener.click(parcel)
                }
                more.setOnClickListener {
                    clickListener.dots(more, parcel)
                }

                parcelCardview.setOnLongClickListener {
                    clickListener.longPress(parcel)
                    true
                }

                parcelProgress.isVisible = parcel.updateStatus == LocalParcelReference.UpdateStatus.INPROGRESS
                parcelStatus.isVisible = parcel.updateStatus != LocalParcelReference.UpdateStatus.INPROGRESS

                val faseRaw = parcel.ultimoEstado?.fase
                val faseNumber : Int? = if(faseRaw == "?") null else parcel.ultimoEstado?.fase?.toIntOrNull()
                val fase = if (faseNumber != null)
                    ParcelDetailStatus.Fase.fromFase(faseNumber)
                else ParcelDetailStatus.Fase.OTHER

                if (parcel.updateStatus == LocalParcelReference.UpdateStatus.OK) {
                    parcelStatus.setImageResource(fase.drawable)
                } else if (parcel.updateStatus == LocalParcelReference.UpdateStatus.ERROR) {
                    parcelStatus.setImageResource(R.drawable.ic_error_red)
                }
                else if(parcel.updateStatus == LocalParcelReference.UpdateStatus.UNKNOWN){
                    parcelStatus.setImageResource(R.drawable.timeline_icon_unknown)
                }

                var lastCheckedValue = parcel.lastChecked

                if (lastCheckedValue != null && lastCheckedValue > 0) {
                    lastChecked.text = context.getString(R.string.lastchecked, dateFormat.format(Date(lastCheckedValue)))
                }
                lastChecked.isVisible = lastCheckedValue != null && lastCheckedValue > 0
            }
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

    fun getAllItems(): List<LocalParcelReference> = allItems
}
