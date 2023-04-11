package net.kelmer.correostracker.detail

import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor
import net.kelmer.correostracker.dataApi.model.dto.ParcelDetailDTO
import net.kelmer.correostracker.dataApi.model.local.LocalParcelReference
import net.kelmer.correostracker.dataApi.model.remote.CorreosApiEvent
import net.kelmer.correostracker.dataApi.repository.correos.CorreosRepository
import net.kelmer.correostracker.dataApi.repository.local.LocalParcelRepository
import net.kelmer.correostracker.util.SchedulerProvider
import net.kelmer.correostracker.viewmodel.AutoDisposeViewModel
import timber.log.Timber
import javax.inject.Inject

class ParcelDetailViewModel @Inject constructor(
    private val parcelCode: String,
    localParcelRepository: LocalParcelRepository,
    private val correosRepository: CorreosRepository,
    schedulerProvider: SchedulerProvider
) : AutoDisposeViewModel() {

    init {
        Timber.i("Detail - ViewModel Created!")
    }

    private val refreshSubject = BehaviorProcessor.createDefault(Unit)

    val stateOnceAndStream = refreshSubject
        .observeOn(schedulerProvider.io())
        .switchMap {
            correosRepository.getParcelStatus(parcelCode).toFlowable()
                .zipWith(localParcelRepository.getParcel(parcelCode)) { correosParcel, localParcel ->
                    ParcelDetailDTO(
                        localParcel.parcelName,
                        localParcel.trackingCode,
                        localParcel.ancho ?: "",
                        localParcel.alto ?: "",
                        localParcel.largo ?: "",
                        localParcel.peso ?: "",
                        localParcel.refCliente ?: "",
                        localParcel.codProducto ?: "",
                        localParcel.fechaCalculada ?: "",
                        correosParcel.eventos ?: emptyList()
                    )
                }
                .map { State(parcelDetail = it, trackingCode = parcelCode) }
                .startWith(State(isLoading = true, trackingCode = parcelCode))
        }
        .subscribeOn(schedulerProvider.io())
        .startWith(State(isLoading = true, trackingCode = parcelCode))
        .onErrorReturn { throwable -> State(error = throwable, trackingCode = parcelCode) }
        .distinctUntilChanged()
        .replay(1)
        .connectInViewModelScope()

    fun refresh() = refreshSubject.onNext(Unit)

    data class State(
        val trackingCode: String,
        val parcelDetail: ParcelDetailDTO? = null,
        val isLoading: Boolean = false,
        val error: Throwable? = null
    )
}
