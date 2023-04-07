package net.kelmer.correostracker.list.adapter

import android.view.View
import com.xwray.groupie.Item
import com.xwray.groupie.viewbinding.BindableItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import net.kelmer.correostracker.dataApi.model.local.LocalParcelReference
import net.kelmer.correostracker.ui.Fase
import net.kelmer.correostracker.list.R
import net.kelmer.correostracker.list.databinding.RvParcelItemBinding
import net.kelmer.correostracker.util.ext.isVisible
import java.text.SimpleDateFormat
import java.util.Date

class ParcelListItem @AssistedInject constructor(
    @Assisted private val parcel: LocalParcelReference,
    @Assisted private val clickListener: ParcelClickListener
) : BindableItem<RvParcelItemBinding>(){

    private val dateFormat = SimpleDateFormat("dd/MM/yyy HH:mm:ss")

    override fun bind(viewBinding: RvParcelItemBinding, position: Int) {
        with(viewBinding) {
            parcelName.text = parcel.parcelName
            parcelCode.text = parcel.trackingCode

            ultimoEstado.text = parcel.ultimoEstado?.buildUltimoEstado()
                ?: ultimoEstado.context.getString(R.string.status_unknown)

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
            val faseNumber: Int? = if (faseRaw == "?") null else faseRaw?.toIntOrNull()
            val fase = if (faseNumber != null) Fase.fromFase(faseNumber) else Fase.OTHER

            if (parcel.updateStatus == LocalParcelReference.UpdateStatus.OK) {
                parcelStatus.setImageResource(fase.drawable)
            } else if (parcel.updateStatus == LocalParcelReference.UpdateStatus.ERROR) {
                parcelStatus.setImageResource(R.drawable.ic_error_red)
            } else if (parcel.updateStatus == LocalParcelReference.UpdateStatus.UNKNOWN) {
                parcelStatus.setImageResource(R.drawable.timeline_icon_unknown)
            }

            val lastCheckedValue = parcel.lastChecked

            if (lastCheckedValue != null && lastCheckedValue > 0) {
                lastChecked.text = lastChecked.context
                    .getString(R.string.lastchecked, dateFormat.format(Date(lastCheckedValue)))
            }
            lastChecked.isVisible = lastCheckedValue != null && lastCheckedValue > 0
        }
    }

    override fun isSameAs(other: Item<*>): Boolean {
        return other is ParcelListItem && parcel.code == other.parcel.code
    }

    override fun getLayout(): Int = R.layout.rv_parcel_item

    override fun initializeViewBinding(view: View): RvParcelItemBinding = RvParcelItemBinding.bind(view)

    /**
     * Factory to create [ParcelListItem]
     */
    @AssistedFactory
    interface Factory {
        /**
         * Creates [ParcelListItem]
         */
        fun create(
           parcel: LocalParcelReference,
           parcelClickListener: ParcelClickListener
        ): ParcelListItem
    }

}
