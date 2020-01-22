package net.kelmer.correostracker.ui.detail

import androidx.lifecycle.MutableLiveData
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import net.kelmer.correostracker.base.BaseViewModel
import net.kelmer.correostracker.data.Result
import net.kelmer.correostracker.data.model.dto.ParcelDetailDTO
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.data.model.remote.CorreosApiParcel
import net.kelmer.correostracker.data.repository.correos.CorreosRepository
import net.kelmer.correostracker.data.repository.local.LocalParcelRepository
import net.kelmer.correostracker.ext.toResult
import javax.inject.Inject


class ParcelDetailViewModel @Inject constructor(val localParcelRepository: LocalParcelRepository,
                                                val correosRepository: CorreosRepository) : BaseViewModel() {


    val parcel: MutableLiveData<Result<ParcelDetailDTO>> = MutableLiveData()


    fun getParcel(parcelCode: String) {

        correosRepository.getParcelStatus(parcelCode)
                .compose(networkInteractor.single())
                .toFlowable()
                .zipWith(
                        localParcelRepository.getParcel(parcelCode)
                        ,
                        BiFunction<CorreosApiParcel, LocalParcelReference, ParcelDetailDTO> { correosParcel, localParcel ->
                            ParcelDetailDTO(
                                    localParcel.parcelName,
                                    localParcel.code,
                                    localParcel.ancho ?: "",
                                    localParcel.alto ?: "",
                                    localParcel.largo ?: "",
                                    localParcel.peso ?: "",
                                    localParcel.refCliente ?: "",
                                    localParcel.codProducto ?: "",
                                    localParcel.fechaCalculada ?: "",
                                    correosParcel.eventos ?: emptyList())
                        }

                )
                .toResult(schedulerProvider)
                .subscribeBy(onNext = {
                    parcel.value = it
                })
                .addTo(disposables)
    }
}