package net.kelmer.correostracker.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Flowable
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import net.kelmer.correostracker.base.BaseViewModel
import net.kelmer.correostracker.data.Resource
import net.kelmer.correostracker.data.model.dto.ParcelDetailDTO
import net.kelmer.correostracker.data.repository.correos.CorreosRepository
import net.kelmer.correostracker.data.repository.local.LocalParcelRepository
import net.kelmer.correostracker.usecases.details.GetParcelUseCase
import net.kelmer.correostracker.util.ext.toResource
import net.kelmer.correostracker.viewmodel.AutoDisposeViewModel
import java.util.concurrent.Flow
import javax.inject.Inject

@HiltViewModel
class ParcelDetailViewModel @Inject constructor(
    private val localParcelRepository: LocalParcelRepository,
    private val correosRepository: CorreosRepository,
    savedStateHandle: SavedStateHandle
) : AutoDisposeViewModel() {

    private val parcelCode: String = checkNotNull(savedStateHandle.get<String>(DetailFragment.KEY_PARCELCODE))

    private val reloadTrigger: PublishProcessor<Unit> = PublishProcessor.create()

    val stateOnceAndStream: Flowable<Resource<State>> = reloadTrigger
        .switchMap {
            correosRepository
                .getParcelStatus(parcelCode).toFlowable()
                .zipWith(localParcelRepository.getParcel(parcelCode)) { correosParcel, localParcel ->
                    State(
                        ParcelDetailDTO(
                            name = localParcel.parcelName,
                            code = localParcel.trackingCode,
                            ancho = localParcel.ancho ?: "",
                            alto = localParcel.alto ?: "",
                            largo = localParcel.largo ?: "",
                            peso = localParcel.peso ?: "",
                            refCliente = localParcel.refCliente ?: "",
                            codProducto = localParcel.codProducto ?: "",
                            fechaCalculada = localParcel.fechaCalculada ?: "",
                            states = correosParcel.eventos ?: emptyList()
                        )
                    )
                }
        }
        .subscribeOn(Schedulers.io())
        .distinctUntilChanged()
        .toResource()
        .replay(1)
        .connectInViewModelScope()

    fun refresh() {
        reloadTrigger.onNext(Unit)
    }

    data class State(val parcel: ParcelDetailDTO)
}
