package net.kelmer.correostracker.detail.adapter

import android.view.View
import androidx.core.content.ContextCompat
import com.xwray.groupie.viewbinding.BindableItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import net.kelmer.correostracker.data.model.dto.ParcelDetailDTO
import net.kelmer.correostracker.data.model.dto.ParcelDetailStatus
import net.kelmer.correostracker.data.model.remote.CorreosApiEvent
import net.kelmer.correostracker.details.R
import net.kelmer.correostracker.details.databinding.RvDetailItemBinding
import net.kelmer.correostracker.util.ext.isVisible

class DetailTimelineItem @AssistedInject constructor(
    @Assisted private val event: CorreosApiEvent
) : BindableItem<RvDetailItemBinding>() {
    override fun bind(viewBinding: RvDetailItemBinding, position: Int) {
        val faseNumber = event.fase?.toIntOrNull()
        val fase =
            if (faseNumber != null) ParcelDetailStatus.Fase.fromFase(faseNumber) else ParcelDetailStatus.Fase.OTHER

        viewBinding.apply {
            timeMarker.setMarker(ContextCompat.getDrawable(root.context, fase.drawable))

            textTimelineTitle.text = event.desTextoResumen
            textTimelineDate.text = event.fecEvento
            textTimelineTime.text = event.horEvento
            textTimelineDescription.text = event.desTextoAmpliado
            locationContainer.isVisible = !event.unidad.isNullOrBlank()
            textTimelineLocation.text = event.unidad
        }
    }

    override fun getLayout(): Int = R.layout.rv_detail_item

    override fun initializeViewBinding(view: View): RvDetailItemBinding = RvDetailItemBinding.bind(view)

    @AssistedFactory
    interface Factory {
        fun create(parcel: CorreosApiEvent): DetailTimelineItem
    }
}
