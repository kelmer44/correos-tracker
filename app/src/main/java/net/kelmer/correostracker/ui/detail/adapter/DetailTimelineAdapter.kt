package net.kelmer.correostracker.ui.detail.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.rv_detail_item.view.*
import net.kelmer.correostracker.R
import net.kelmer.correostracker.data.model.dto.ParcelDetailStatus
import net.kelmer.correostracker.data.model.remote.CorreosApiEvent


class DetailTimelineAdapter : RecyclerView.Adapter<DetailTimelineAdapter.TimeLineViewHolder>() {

    private var statuses: List<CorreosApiEvent> = emptyList()

    override fun getItemCount() = statuses.size

    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {
        val status = statuses[position]
        holder.bindStatus(status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
                .inflate(R.layout.rv_detail_item,
                        parent, false)
        return TimeLineViewHolder(inflatedView)
    }

    fun updateStatus(updatedList: List<CorreosApiEvent>) {
        this.statuses = updatedList
        notifyDataSetChanged()
    }

    class TimeLineViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        private var status: CorreosApiEvent? = null

        fun bindStatus(status: CorreosApiEvent) {
            this.status = status
            val faseNumber = status.fase?.toInt()
            val fase = if (faseNumber != null) ParcelDetailStatus.Fase.fromFase(faseNumber) else ParcelDetailStatus.Fase.OTHER
            view.time_marker.setMarker(ContextCompat.getDrawable(view.context,fase.drawable))

            view.text_timeline_title.text = status.desTextoResumen
            view.text_timeline_date.text = status.fecEvento
            view.text_timeline_time.text = status.horEvento
            view.text_timeline_description.text = status.desTextoAmpliado
            view.text_timeline_location.text = status.unidad
        }

    }
}
