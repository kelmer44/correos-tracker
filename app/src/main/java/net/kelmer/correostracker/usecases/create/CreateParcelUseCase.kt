package net.kelmer.correostracker.usecases.create

import io.reactivex.Single
import net.kelmer.correostracker.base.usecase.rx.RxSingleUseCase
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.data.model.remote.CorreosApiParcel
import net.kelmer.correostracker.data.network.exception.CorreosExceptionFactory
import net.kelmer.correostracker.data.repository.correos.CorreosRepository
import net.kelmer.correostracker.data.repository.local.LocalParcelRepository
import java.util.Date
import javax.inject.Inject

class CreateParcelUseCase @Inject constructor(private val localParcelRepository: LocalParcelRepository,
                                              private val remoteParcelRepository: CorreosRepository) : RxSingleUseCase<CreateParcelUseCase.Params, LocalParcelReference>() {

    data class Params(val localParcelReference: LocalParcelReference)

    override fun buildUseCase(params: Params): Single<LocalParcelReference> {
//        val localParcelReference = params.localParcelReference
//        return remoteParcelRepository.retrieveParcel(params.localParcelReference.code)
//                .flatMap { element ->
//                    //Mapping errors to a proper exception
//                    val error = element.error
//                    if (error != null && error.codError != "0") {
//                        Single.error(CorreosExceptionFactory.byCode(error.codError, error.desError))
//                    } else {
//                        localParcelReference.let { parcel ->
//                            parcel.ultimoEstado = element.eventos?.lastOrNull()
//                            parcel.lastChecked = Date().time
//                            parcel.alto = element.alto
//                            parcel.ancho = element.ancho
//                            parcel.largo = element.largo
//                            parcel.codProducto = element.codProducto
//                            parcel.refCliente = element.refCliente
//                            parcel.peso = element.peso
//                            parcel.fechaCalculada = element.fechaCalculada
//                        }
//                        localParcelRepository.saveParcel(localParcelReference)
//                                .toSingle {
//                                    element
//                                }
//                    }
//                }
        return localParcelRepository.saveParcel(params.localParcelReference).andThen(localParcelRepository.getParcel(params.localParcelReference.code)).firstOrError()
    }
}