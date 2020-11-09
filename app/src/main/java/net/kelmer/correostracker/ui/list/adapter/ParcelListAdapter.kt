package net.kelmer.correostracker.ui.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.rv_parcel_item.view.last_checked
import kotlinx.android.synthetic.main.rv_parcel_item.view.more
import kotlinx.android.synthetic.main.rv_parcel_item.view.parcel_cardview
import kotlinx.android.synthetic.main.rv_parcel_item.view.parcel_code
import kotlinx.android.synthetic.main.rv_parcel_item.view.parcel_name
import kotlinx.android.synthetic.main.rv_parcel_item.view.parcel_progress
import kotlinx.android.synthetic.main.rv_parcel_item.view.parcel_stance
import kotlinx.android.synthetic.main.rv_parcel_item.view.parcel_status
import kotlinx.android.synthetic.main.rv_parcel_item.view.ultimo_estado
import net.kelmer.correostracker.R
import net.kelmer.correostracker.data.model.dto.ParcelDetailStatus
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.ext.isVisible
import java.text.SimpleDateFormat
import java.util.Date


/**z
 * Created by gabriel on 25/03/2018.
 */
class ParcelListAdapter constructor(
        private val clickListener: ParcelClickListener
) : RecyclerView.Adapter<ParcelListAdapter.ViewHolder>() {


    private var allItems = mutableListOf<LocalParcelReference>()
    private var filteredItems = mutableListOf<LocalParcelReference>()


    override fun getItemCount(): Int =
            filteredItems.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.rv_parcel_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filteredItems[position], clickListener)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(parcel: LocalParcelReference,
                 clickListener: ParcelClickListener) = with(itemView) {

            parcel_name.text = parcel.parcelName
            parcel_code.text = parcel.trackingCode

            ultimo_estado.text = parcel.ultimoEstado?.buildUltimoEstado()
                    ?: context.getString(R.string.status_unknown)

            when (parcel.stance) {
                LocalParcelReference.Stance.INCOMING -> {
                    parcel_stance.setText(R.string.incoming)
                }
                LocalParcelReference.Stance.OUTGOING -> {
                    parcel_stance.setText(R.string.outgoing)
                }
            }

            parcel_cardview.setOnClickListener {
                clickListener.click(parcel)
            }
            more.setOnClickListener {
                clickListener.dots(more, parcel)
            }

            parcel_cardview.setOnLongClickListener {
                clickListener.longPress(parcel)
                true
            }

            parcel_progress.isVisible = parcel.isLoading
            parcel_status.isVisible = !parcel.isLoading

            val faseNumber = parcel.ultimoEstado?.fase?.toInt()
            val fase = if (faseNumber != null) ParcelDetailStatus.Fase.fromFase(faseNumber) else ParcelDetailStatus.Fase.OTHER

            parcel_status.setImageResource(fase.drawable)

            var lastChecked = parcel.lastChecked

            if (lastChecked != null && lastChecked > 0) {
                //                val relativeText = DateUtils.getRelativeTimeSpanString(lastChecked, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
                //                last_checked.text = context.getString(R.string.lastchecked, relativeText)
                last_checked.text = context.getString(R.string.lastchecked, dateFormat.format(Date(lastChecked)))

            }
            last_checked.isVisible = lastChecked != null && lastChecked > 0
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

    val dateFormat = SimpleDateFormat("dd/MM/yyy HH:mm:ss")
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