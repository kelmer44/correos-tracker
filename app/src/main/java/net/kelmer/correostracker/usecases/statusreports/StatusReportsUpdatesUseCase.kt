package net.kelmer.correostracker.usecases.statusreports

import io.reactivex.Flowable
import net.kelmer.correostracker.base.usecase.rx.RxFlowableUseCase
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.data.model.remote.CorreosApiParcel
import javax.inject.Inject

class StatusReportsUpdatesUseCase @Inject constructor() : RxFlowableUseCase<StatusReportsUpdatesUseCase.Params, CorreosApiParcel>() {



    data class Params(val items: List<LocalParcelReference>)

    override fun buildUseCase(params: Params): Flowable<CorreosApiParcel> {
        TODO("Not yet implemented")
    }
}