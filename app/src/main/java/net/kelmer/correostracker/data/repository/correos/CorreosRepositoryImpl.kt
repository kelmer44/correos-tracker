package net.kelmer.correostracker.data.repository.correos

import io.reactivex.Flowable
import io.reactivex.Single
import net.kelmer.correostracker.BuildConfig
import net.kelmer.correostracker.BuildConfig.DEBUG
import net.kelmer.correostracker.data.model.local.LocalParcelDao
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.data.model.remote.CorreosApiEvent
import net.kelmer.correostracker.data.model.remote.CorreosApiParcel
import net.kelmer.correostracker.data.model.remote.Error
import net.kelmer.correostracker.data.model.remote.unidad.Unidad
import net.kelmer.correostracker.data.model.remote.v1.Parcel
import net.kelmer.correostracker.data.model.remote.v1.Shipment
import net.kelmer.correostracker.data.network.correos.CorreosApi
import net.kelmer.correostracker.data.network.correos.CorreosV1
import net.kelmer.correostracker.data.network.correos.Unidades
import net.kelmer.correostracker.data.network.exception.CorreosExceptionFactory
import timber.log.Timber
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CorreosRepositoryImpl @Inject constructor(
    val correosApi: CorreosV1,
    val unidades: Unidades,
    val dao: LocalParcelDao
) : CorreosRepository {


    fun parcelAndUnits(parcelCode: String): Single<Pair<Shipment, Map<String, Unidad>>> {
       return correosApi.getParcelStatus(parcelCode)
            .map { parcel ->
                parcel.shipment?.firstOrNull()
            }
            .flatMap {shipment ->
                val map: List<Single<Unidad>> =
                    shipment.events.filter { it.codired != null }.map { event -> unidades.getUnidad(event.codired!!) }
               Single.zip(map) { unidades: Array<Any> ->
                   unidades.map { it as Unidad }.associateBy { it.officeId }
               }
                   .map { shipment to it }
            }
    }

    override fun retrieveParcel(parcelCode: String): Single<CorreosApiParcel> {
        return parcelAndUnits(parcelCode)
            .map { (shipment, unidades) ->
                    CorreosApiParcel(
                        codEnvio = shipment.shipmentCode,
                        refCliente = null,
                        codProducto = null,
                        fechaCalculada = shipment.dateDeliverySum,
                        error = shipment.error?.let {
                            net.kelmer.correostracker.data.model.remote.Error(
                                codError = it.errorCode,
                                desError = null
                            )
                        },
                        eventos = shipment.events
                            .filter { !it.summaryText.isNullOrBlank() }
                            .map {
                                CorreosApiEvent(
                                    fecEvento = it.eventDate,
                                    horEvento = it.eventTime,
                                    fase = it.phase,
                                    desTextoResumen = it.summaryText,
                                    desTextoAmpliado = it.extendedText,
                                    unidad = unidades[it.codired]?.name
                                )
                            }
                    )
            }
    }

    override fun getParcelStatus(parcelId: String): Single<CorreosApiParcel> {
        var parcelReference: LocalParcelReference? = null
        return dao.getParcelSync(parcelId)
            .doOnSuccess {
                parcelReference = it
            }
            .flatMap {
                retrieveParcel(parcelId)
            }
            .flatMap { element ->
                // Mapping errors to a proper exception
                val error = element.error
                // If theres actually an error in the api response, we map it
                if (error != null && error.codError != "0") {

                    Single.error(CorreosExceptionFactory.byCode(error.codError, error.desError))
                } else {
                    Single.just(element)
                }
            }
            .doOnError {
                val p = parcelReference
                if (p != null) {
                    p.updateStatus = LocalParcelReference.UpdateStatus.ERROR
                    dao.saveParcel(p)
                }
            }
            .doOnSuccess {
                parcelReference?.let { p ->
                    p.ultimoEstado = it.eventos?.lastOrNull()
                    p.lastChecked = Date().time
                    p.alto = it.alto
                    p.ancho = it.ancho
                    p.largo = it.largo
                    p.codProducto = it.codProducto
                    p.refCliente = it.refCliente
                    p.peso = it.peso
                    p.fechaCalculada = it.fechaCalculada
                    p.updateStatus = LocalParcelReference.UpdateStatus.OK
                    var value = dao.saveParcel(p)
                    Timber.w("Saving $p to database! $value saved")
                }
            }
    }
}
