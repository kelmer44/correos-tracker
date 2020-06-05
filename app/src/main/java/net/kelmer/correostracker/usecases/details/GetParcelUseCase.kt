package net.kelmer.correostracker.usecases.details

import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import net.kelmer.correostracker.base.usecase.rx.RxFlowableUseCase
import net.kelmer.correostracker.base.usecase.rx.RxSingleUseCase
import net.kelmer.correostracker.data.model.dto.ParcelDetailDTO
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.data.model.remote.CorreosApiParcel
import net.kelmer.correostracker.data.repository.correos.CorreosRepository
import net.kelmer.correostracker.data.repository.local.LocalParcelRepository
import net.kelmer.correostracker.ext.toResult
import net.kelmer.correostracker.ext.withNetwork
import javax.inject.Inject

class GetParcelUseCase @Inject constructor(val localParcelRepository: LocalParcelRepository,
                                           val correosRepository: CorreosRepository): RxFlowableUseCase<GetParcelUseCase.Params, ParcelDetailDTO>() {

    data class Params(val parcelCode: String)

    override fun buildUseCase(params: Params): Flowable<ParcelDetailDTO> {
        return correosRepository.getParcelStatus(params.parcelCode)
                .withNetwork(networkInteractor)
                .toFlowable()
                .zipWith(
                        localParcelRepository.getParcel(params.parcelCode)
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
    }
}