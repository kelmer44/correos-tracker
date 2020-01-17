package net.kelmer.correostracker.data.repository.correos

import io.reactivex.Flowable
import io.reactivex.Single
import net.kelmer.correostracker.data.model.local.LocalParcelDao
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.data.model.remote.CorreosApiParcel
import net.kelmer.correostracker.data.network.correos.CorreosApi
import net.kelmer.correostracker.data.network.exception.CorreosException
import net.kelmer.correostracker.data.network.exception.CorreosExceptionFactory
import timber.log.Timber
import java.util.*

class CorreosRepositoryImpl(val correosApi: CorreosApi, val dao: LocalParcelDao) : CorreosRepository {

    override fun getParcelStatus(parcelId: String): Single<CorreosApiParcel> {

        var parcelReference: LocalParcelReference? = null

        return dao.getParcelSync(parcelId)
                .doOnSuccess {
                    parcelReference = it
                }
                .flatMap {
                    correosApi.getParcelStatus(parcelId)
                }
                .map {
                    it.first()
                }
                .flatMap { element ->
                    //Mapping errors to a proper exception
                    val error = element.error
                    if (error != null && error.codError != "0") {
                        Single.error(CorreosExceptionFactory.byCode(error.codError, error.desError))
                    } else {
                        Single.just(element)
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
                        var value = dao.saveParcel(p)
                        Timber.w("Saving $p to database! $value saved")
                    }

                }
    }


    companion object {
        private var instance: CorreosRepositoryImpl? = null

        fun getInstance(correosApi: CorreosApi, correosDao: LocalParcelDao): CorreosRepositoryImpl {
            if (instance == null) {
                instance = CorreosRepositoryImpl(correosApi, correosDao)
            }
            return instance!!
        }
    }

}