package net.kelmer.correostracker.usecases.list

import io.reactivex.Flowable
import net.kelmer.correostracker.base.usecase.rx.RxFlowableUseCase
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.data.repository.local.LocalParcelRepository
import javax.inject.Inject

class GetParcelListUseCase @Inject constructor(private val localParcelRepository: LocalParcelRepository) :
    RxFlowableUseCase<Unit, List<LocalParcelReference>>() {
    override fun buildUseCase(params: Unit): Flowable<List<LocalParcelReference>> {
        return localParcelRepository.getParcels()
    }
}
