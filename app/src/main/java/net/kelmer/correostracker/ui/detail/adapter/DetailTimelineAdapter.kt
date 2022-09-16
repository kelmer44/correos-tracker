package net.kelmer.correostracker.ui.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import net.kelmer.correostracker.data.model.dto.ParcelDetailStatus
import net.kelmer.correostracker.data.model.remote.CorreosApiEvent
import net.kelmer.correostracker.databinding.RvDetailItemBinding
import net.kelmer.correostracker.util.ext.isVisible

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
                val inflatedView = RvDetailItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
                return TimeLineViewHolder(inflatedView)
            }
        }

        fun bindStatus(status: CorreosApiEvent) {
            this.status = status
            val faseNumber = status.fase?.toIntOrNull()
            val fase = if (faseNumber != null) ParcelDetailStatus.Fase.fromFase(faseNumber) else ParcelDetailStatus.Fase.OTHER

            binding.apply {
                timeMarker.setMarker(ContextCompat.getDrawable(root.context, fase.drawable))

                textTimelineTitle.text = status.desTextoResumen
                textTimelineDate.text = status.fecEvento
                textTimelineTime.text = status.horEvento
                textTimelineDescription.text = status.desTextoAmpliado
                locationContainer.isVisible = !status.unidad.isNullOrBlank()
                textTimelineLocation.text = status.unidad
            }
        }
    }
}
