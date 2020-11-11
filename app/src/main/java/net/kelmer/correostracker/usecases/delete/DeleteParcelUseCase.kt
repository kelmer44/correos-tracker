package net.kelmer.correostracker.usecases.delete

import io.reactivex.Single
import net.kelmer.correostracker.base.usecase.rx.RxSingleUseCase
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.data.repository.local.LocalParcelRepository
import javax.inject.Inject

class DeleteParcelUseCase @Inject constructor(private val localParcelRepository: LocalParcelRepository) : RxSingleUseCase<DeleteParcelUseCase.Params, Unit>() {

    data class Params(val parcel: LocalParcelReference)

    override fun buildUseCase(params: Params): Single<Unit> {
        return localParcelRepository.deleteParcel(params.parcel).toSingleDefault(Unit)
    }
}
