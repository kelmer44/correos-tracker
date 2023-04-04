package net.kelmer.correostracker.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import net.kelmer.correostracker.dataApi.model.exception.CorreosExceptionFactory
import net.kelmer.correostracker.dataApi.model.exception.WrongCodeException
import net.kelmer.correostracker.dataApi.model.local.LocalParcelDao
import net.kelmer.correostracker.dataApi.model.local.LocalParcelReference
import net.kelmer.correostracker.dataApi.model.local.LocalUnidad
import net.kelmer.correostracker.dataApi.model.local.LocalUnidadDao
import net.kelmer.correostracker.dataApi.model.remote.CorreosApiEvent
import net.kelmer.correostracker.dataApi.model.remote.CorreosApiParcel
import net.kelmer.correostracker.dataApi.model.remote.Error
import net.kelmer.correostracker.dataApi.model.remote.unidad.Unidad
import net.kelmer.correostracker.dataApi.model.remote.v1.Shipment
import net.kelmer.correostracker.dataApi.model.remote.v1.ShipmentEvent
import net.kelmer.correostracker.data.remote.CorreosV1
import net.kelmer.correostracker.data.remote.UnidadesApi
import net.kelmer.correostracker.dataApi.repository.correos.CorreosRepository
import timber.log.Timber
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CorreosRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val correosApi: CorreosV1,
    private val unidadDao: LocalUnidadDao,
    private val unidades: UnidadesApi,
    private val dao: LocalParcelDao
) : CorreosRepository {


    private fun parcelAndUnits(parcelCode: String): Single<Pair<Shipment, Map<String, LocalUnidad>>> {
        return correosApi.getParcelStatus(parcelCode)
            .map { parcel ->
                parcel.shipment?.firstOrNull() ?: throw WrongCodeException(
                    parcelCode,
                    context.getString(R.string.unrecognized_code)
                )
            }
            .flatMap { shipment ->
                val map =
                    shipment.events.mapNotNull { it.codired }.map { codigo -> getUnidad(codigo) }
                if (map.isNotEmpty()) {
                    Maybe
                        .zip(map) { unidades: Array<Any> ->
                            unidades
                                .mapNotNull { it as? LocalUnidad }
                                .associateBy { it.officeId }
                        }
                        .toSingle(emptyMap())
                } else {
                    Single.just(emptyMap())
                }
                    .map { shipment to it }
            }
    }

    private fun getUnidad(officeId: String): Maybe<LocalUnidad> {
        return unidadDao.getUnidad(officeId)
            .doOnComplete { Timber.w("CACHETEST - Hit from local cache!") }
            .switchIfEmpty(
                unidades.getUnidad(officeId)
                    .filter { it.officeId != null }
                    .flatMap {
                        Timber.w("CACHETEST - Cache miss saving from remote!")
                        val localUnidad = LocalUnidad(officeId, it.officeType, it.cityName)
                        unidadDao.saveUnidad(localUnidad)
                            .toMaybe<Unit>()
                            .flatMap { unidadDao.getUnidad(officeId) }
                    }
            )
    }

    override fun retrieveParcel(parcelCode: String): Single<CorreosApiParcel> {
        return parcelAndUnits(parcelCode).map { (shipment, unidades) ->
            CorreosApiParcel(codEnvio = shipment.shipmentCode,
                refCliente = null,
                codProducto = null,
                fechaCalculada = shipment.dateDeliverySum,
                error = shipment.error?.let {
                    Error(
                        codError = it.errorCode, desError = null
                    )
                },
                eventos = fixSummary(shipment.events).map {
                    CorreosApiEvent(
                        fecEvento = it.eventDate,
                        horEvento = it.eventTime,
                        fase = it.phase,
                        desTextoResumen = it.summaryText,
                        desTextoAmpliado = it.extendedText,
                        unidad = unidades[it.codired]?.name
                    )
                })
        }
    }

    private fun fixSummary(list: List<ShipmentEvent>) =
        list.mapIndexed { index, event ->
            var event = event
            if (event.summaryText.isNullOrBlank() && index != 0) {
                event = event.copy(summaryText = list[index - 1].summaryText)
            }
            if (event.extendedText.isNullOrBlank() && index != 0) {
                event = event.copy(summaryText = list[index - 1].extendedText)
            }
            event
        }


    override fun getParcelStatus(parcelId: String): Single<CorreosApiParcel> {
        var parcelReference: LocalParcelReference? = null
        return dao.getParcelSync(parcelId)
            //this magic line adds synchronicity :shrug
            .delay(0, TimeUnit.MILLISECONDS, Schedulers.computation())
            .doOnSuccess {
                parcelReference = it
            }.flatMap {
                retrieveParcel(parcelId)
            }.flatMap { element ->
                // Mapping errors to a proper exception
                val error = element.error
                // If theres actually an error in the api response, we map it
                if (error != null && error.codError != "0") {
                    Single.error(CorreosExceptionFactory.byCode(error.codError, error.desError))
                } else {
                    Single.just(element)
                }
            }.doOnError {
                val p = parcelReference
                if (p != null) {
                    p.updateStatus = LocalParcelReference.UpdateStatus.ERROR
                    dao.saveParcel(p)
                }
            }.doOnSuccess {
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
