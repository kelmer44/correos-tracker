package net.kelmer.correostracker.usecases.statusreports

import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import net.kelmer.correostracker.base.usecase.rx.RxUseCase
import net.kelmer.correostracker.data.Resource
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.data.model.remote.CorreosApiParcel
import net.kelmer.correostracker.data.repository.correos.CorreosRepository
import net.kelmer.correostracker.data.repository.local.LocalParcelRepository
import timber.log.Timber
import javax.inject.Inject

class StatusReportsUpdatesUseCase @Inject constructor(
    private val localParcelRepository: LocalParcelRepository,
    private val parcelRepository: CorreosRepository
) : RxUseCase<Unit, CorreosApiParcel>() {

    override fun execute(params: Unit, onNext: (Resource<CorreosApiParcel>) -> Unit) {

        localParcelRepository.getParcels().firstOrError()
            .onErrorReturnItem(emptyList())
            .toFlowable().flatMap {
                val map = it.mapIndexed { idx, item ->
                    item.updateStatus = LocalParcelReference.UpdateStatus.INPROGRESS
                    localParcelRepository.saveParcel(item)
                        .andThen(
                            parcelRepository.getParcelStatus(item.trackingCode)
                        )
                        .doOnSubscribe {
                            Timber.w("Subscribed")
                        }
                        .map {
                            Resource.success(it)
                        }
                        .onErrorReturn {
                            Resource.failure(it)
                        }
                }
                Single.merge(map)
            }
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribeBy(onNext = onNext)
            .track()
    }
}
