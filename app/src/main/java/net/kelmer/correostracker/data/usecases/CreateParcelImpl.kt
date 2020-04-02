package net.kelmer.correostracker.data.usecases

import io.reactivex.Single
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.data.model.remote.CorreosApiParcel
import net.kelmer.correostracker.data.network.correos.CorreosApi
import net.kelmer.correostracker.data.network.exception.CorreosExceptionFactory
import net.kelmer.correostracker.data.repository.local.LocalParcelRepository
import java.util.Date
import javax.inject.Inject

class CreateParcelImpl @Inject constructor(
    val localParcelRepository: LocalParcelRepository,
    val correosApi: CorreosApi
) : CreateParcel {
    override fun execute(
        localParcelReference: LocalParcelReference
    ): Single<CorreosApiParcel> {
        return correosApi.getParcelStatus(
            localParcelReference.code
        ).map {
            it.firstOrNull()
        }.flatMap { element ->
            //Mapping errors to a proper exception
            val error = element.error
            if (error != null && error.codError != "0") {
                Single.error(CorreosExceptionFactory.byCode(error.codError, error.desError))
            } else {
                localParcelReference.let { parcel ->
                    parcel.ultimoEstado = element.eventos?.lastOrNull()
                    parcel.lastChecked = Date().time
                    parcel.alto = element.alto
                    parcel.ancho = element.ancho
                    parcel.largo = element.largo
                    parcel.codProducto = element.codProducto
                    parcel.refCliente = element.refCliente
                    parcel.peso = element.peso
                    parcel.fechaCalculada = element.fechaCalculada
                }

                localParcelRepository.saveParcel(localParcelReference)
                    .toSingle {
                        element
                    }
            }
        }
    }
}

