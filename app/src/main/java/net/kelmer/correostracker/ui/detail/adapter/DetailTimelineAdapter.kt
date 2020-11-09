package net.kelmer.correostracker.ui.detail.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import net.kelmer.correostracker.data.model.dto.ParcelDetailStatus
import net.kelmer.correostracker.data.model.remote.CorreosApiEvent
import net.kelmer.correostracker.databinding.RvDetailItemBinding
import net.kelmer.correostracker.ext.isVisible


class DetailTimelineAdapter : RecyclerView.Adapter<DetailTimelineAdapter.TimeLineViewHolder>() {

    private var statuses: List<CorreosApiEvent> = emptyList()

    override fun getItemCount() = statuses.size

    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {
        val status = statuses[position]
        holder.bindStatus(status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineViewHolder {
        return TimeLineViewHolder.create(parent)
    }

    fun updateStatus(updatedList: List<CorreosApiEvent>) {
        this.statuses = updatedList
        notifyDataSetChanged()
    }

    class TimeLineViewHolder(val binding: RvDetailItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private var status: CorreosApiEvent? = null

        companion object {
            fun create(parent: ViewGroup): TimeLineViewHolder {
                val inflatedView = RvDetailItemBinding.inflate(LayoutInflater.from(parent.context),
                        parent, false)
                return TimeLineViewHolder(inflatedView)
            }
        }

        fun bindStatus(status: CorreosApiEvent) {
            this.status = status
            val faseNumber = status.fase?.toInt()
            val fase = if (faseNumber != null) ParcelDetailStatus.Fase.fromFase(faseNumber) else ParcelDetailStatus.Fase.OTHER

            binding.timeMarker.setMarker(ContextCompat.getDrawable(binding.root.context, fase.drawable))

            binding.textTimelineTitle.text = status.desTextoResumen
            binding.textTimelineDate.text = status.fecEvento
            binding.textTimelineTime.text = status.horEvento
            binding.textTimelineDescription.text = status.desTextoAmpliado
            binding.locationContainer.isVisible = !status.unidad.isNullOrBlank()
            binding.textTimelineLocation.text = status.unidad
        }

    }
}
