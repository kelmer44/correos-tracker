package net.kelmer.correostracker.usecases.create

import io.reactivex.Single
import net.kelmer.correostracker.base.usecase.rx.RxSingleUseCase
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.data.repository.local.LocalParcelRepository
import javax.inject.Inject

class CreateParcelUseCase @Inject constructor(private val localParcelRepository: LocalParcelRepository) : RxSingleUseCase<CreateParcelUseCase.Params, LocalParcelReference>() {

    data class Params(val localParcelReference: LocalParcelReference)

    override fun buildUseCase(params: Params): Single<LocalParcelReference> {
        return localParcelRepository.saveParcel(params.localParcelReference).andThen(localParcelRepository.getParcel(params.localParcelReference.trackingCode)).firstOrError()
    }
}
