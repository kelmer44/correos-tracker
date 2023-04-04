package net.kelmer.correostracker.list.list

import io.reactivex.Flowable
import net.kelmer.correostracker.usecase.rx.RxFlowableUseCase
import net.kelmer.correostracker.dataApi.model.local.LocalParcelReference
import net.kelmer.correostracker.dataApi.repository.local.LocalParcelRepository
import javax.inject.Inject

class GetParcelListUseCase @Inject constructor(private val localParcelRepository: LocalParcelRepository) :
    RxFlowableUseCase<Unit, List<LocalParcelReference>>() {

    override fun buildUseCase(params: Unit): Flowable<List<LocalParcelReference>> {
        return localParcelRepository.getParcels()
    }
}
