package net.kelmer.correostracker.data.repository.correos

import androidx.lifecycle.Transformations.map
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.internal.operators.single.SingleInternalHelper.toFlowable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import net.kelmer.correostracker.data.model.local.LocalParcelDao
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.data.model.remote.CorreosApiParcel
import net.kelmer.correostracker.data.network.correos.CorreosApi
import timber.log.Timber
import java.util.*

class CorreosRepositoryImpl(val correosApi: CorreosApi, val dao: LocalParcelDao) : CorreosRepository {

    var cache: List<CorreosApiParcel>? = null


    override fun getParcelStatus(parcelId: String): Flowable<CorreosApiParcel> {


        var parcelReference: LocalParcelReference? = null

        return dao.getParcelSync(parcelId)
                .doOnSuccess {
                    parcelReference = it
                }
                .flatMap { (correosApi.getParcelStatus(parcelId)) }
                .map { element -> element[0] }
                .flatMap { element ->
                    //Mapping errors to a proper exception
                    if(element.error!=null && element.error.codError != "0"){
                        Single.error(CorreosException(element.error.codError, element.error.desError))
                    }
                    else {
                        Single.just(element)
                    }
                }
                .doOnSuccess {

                    parcelReference?.let { p ->
                        p.ultimoEstado = it.eventos.last()
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
                .toFlowable()
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