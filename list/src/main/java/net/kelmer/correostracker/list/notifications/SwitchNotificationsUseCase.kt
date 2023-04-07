package net.kelmer.correostracker.list.notifications

import io.reactivex.Single
import net.kelmer.correostracker.usecase.rx.RxSingleUseCase
import net.kelmer.correostracker.dataApi.repository.local.LocalParcelRepository
import javax.inject.Inject

class SwitchNotificationsUseCase @Inject constructor(private val localParcelRepository: LocalParcelRepository) :
    RxSingleUseCase<SwitchNotificationsUseCase.Params, String>() {

    data class Params(val parcelCode: String, val enable: Boolean)

    override fun buildUseCase(params: Params): Single<String> {
        return localParcelRepository.setNotify(params.parcelCode, params.enable).toSingleDefault(params.parcelCode)
    }
}
