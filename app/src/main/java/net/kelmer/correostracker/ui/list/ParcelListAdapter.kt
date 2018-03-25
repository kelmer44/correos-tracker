package net.kelmer.correostracker.ui.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.rv_parcel_item.view.*
import net.kelmer.correostracker.R
import net.kelmer.correostracker.data.model.CorreosApiParcel

/**
 * Created by gabriel on 25/03/2018.
 */
class ParcelListAdapter constructor(val clickListener: (CorreosApiParcel) -> Unit): RecyclerView.Adapter<ParcelListAdapter.ViewHolder>() {

    var items = mutableListOf<CorreosApiParcel>()

    override fun getItemCount(): Int =
        items.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        var view = inflater.inflate(R.layout.rv_parcel_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], clickListener)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(parcel: CorreosApiParcel,
                 clickListener: (CorreosApiParcel) -> Unit) = with(itemView) {

            parcel_name.text = parcel.codEnvio
            parcel_code.text = parcel.codEnvio
            parcel_cardview.setOnClickListener {
                clickListener.invoke(parcel)
            }
        }

    }

    fun updateItems(data: List<CorreosApiParcel>) {
        items = data.toMutableList()
    }
}