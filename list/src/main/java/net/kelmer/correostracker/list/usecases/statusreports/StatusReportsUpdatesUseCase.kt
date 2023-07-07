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
                val map = parcelList.map { item ->
                    val ultimoEstado = item.ultimoEstado
                    item.updateStatus = LocalParcelReference.UpdateStatus.INPROGRESS

                    //Si el paquete ha sido entregado nos podemos saltar la request
                    if (ultimoEstado != null && ultimoEstado.isEntregado()) {
                        return@map Single.just(
                            Resource.success(
                                CorreosApiParcel(
                                    codEnvio = item.trackingCode,
                                    refCliente = item.refCliente,
                                    codProducto = item.codProducto,
                                    fechaCalculada = item.fechaCalculada,
                                    largo = item.largo,
                                    ancho = item.ancho,
                                    alto = item.alto,
                                    peso = item.peso,
                                    eventos = listOf(ultimoEstado),
                                    error = null
                                )
                            )
                        )
                    } else {
                        return@map localParcelRepository.saveParcel(item)
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
                }
                Single.merge(map)
            }
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribeBy(onNext = onNext)
            .track()
    }
}
