package net.kelmer.correostracker.usecases.statusreports

import io.reactivex.Flowable
import io.reactivex.Single
import kotlinx.coroutines.flow.flow
import net.kelmer.correostracker.base.usecase.rx.RxFlowableUseCase
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.data.model.remote.CorreosApiParcel
import net.kelmer.correostracker.data.repository.correos.CorreosRepository
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class StatusReportsUpdatesUseCase @Inject constructor(
        private val parcelRepository: CorreosRepository
) : RxFlowableUseCase<StatusReportsUpdatesUseCase.Params, CorreosApiParcel>() {

    data class Params(val items: List<LocalParcelReference>)

    override fun buildUseCase(params: Params): Flowable<CorreosApiParcel> {
        //FIXME: What happens if one of them fails?
        val map = params.items.mapIndexed { idx, item ->
            parcelRepository.getParcelStatus(item.trackingCode)
        }
        return Single.concat(map)
    }
}