package net.kelmer.correostracker.list.usecases.statusreports

import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import net.kelmer.correostracker.usecase.rx.RxUseCase
import net.kelmer.correostracker.dataApi.Resource
import net.kelmer.correostracker.dataApi.model.local.LocalParcelReference
import net.kelmer.correostracker.dataApi.model.remote.CorreosApiParcel
import net.kelmer.correostracker.dataApi.repository.correos.CorreosRepository
import net.kelmer.correostracker.dataApi.repository.local.LocalParcelRepository
import javax.inject.Inject

class StatusReportsUpdatesUseCase @Inject constructor(
    private val localParcelRepository: LocalParcelRepository,
    private val parcelRepository: CorreosRepository
) : RxUseCase<Unit, CorreosApiParcel>() {

    override fun execute(params: Unit, onNext: (Resource<CorreosApiParcel>) -> Unit) {

        localParcelRepository.getParcels().firstOrError()
            .onErrorReturnItem(emptyList())
            .toFlowable()
            .flatMap { parcelList ->
                val map = parcelList.mapIndexed { _, item ->
                    item.updateStatus = LocalParcelReference.UpdateStatus.INPROGRESS
                    localParcelRepository.saveParcel(item)
                        .andThen(
                            parcelRepository.getParcelStatus(item.trackingCode)
                        )
                        .map { parcel ->
                            Resource.success(parcel)
                        }
                        .onErrorReturn { throwable ->
                            Resource.failure(throwable)
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
