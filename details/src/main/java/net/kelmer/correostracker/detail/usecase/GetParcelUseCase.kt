package net.kelmer.correostracker.detail.usecase

import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import net.kelmer.correostracker.usecase.rx.RxFlowableUseCase
import net.kelmer.correostracker.dataApi.model.dto.ParcelDetailDTO
import net.kelmer.correostracker.dataApi.model.local.LocalParcelReference
import net.kelmer.correostracker.dataApi.model.remote.CorreosApiParcel
import net.kelmer.correostracker.dataApi.repository.correos.CorreosRepository
import net.kelmer.correostracker.dataApi.repository.local.LocalParcelRepository
import net.kelmer.correostracker.util.ext.withNetwork
import javax.inject.Inject

class GetParcelUseCase @Inject constructor(
    val localParcelRepository: LocalParcelRepository,
    val correosRepository: CorreosRepository
) : RxFlowableUseCase<GetParcelUseCase.Params, ParcelDetailDTO>() {

    data class Params(val parcelCode: String)

    override fun buildUseCase(params: Params): Flowable<ParcelDetailDTO> {
        return correosRepository.getParcelStatus(params.parcelCode)
            .withNetwork(networkInteractor)
            .toFlowable()
            .zipWith(
                localParcelRepository.getParcel(params.parcelCode),
                BiFunction<CorreosApiParcel, LocalParcelReference, ParcelDetailDTO> { correosParcel, localParcel ->
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

            )
    }
}
