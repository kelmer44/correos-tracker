package net.kelmer.correostracker.data.repository.correos

import io.reactivex.Single
import net.kelmer.correostracker.data.model.remote.CorreosApiParcel
import net.kelmer.correostracker.data.network.correos.CorreosApi

class CorreosRepositoryImpl(val correosApi: CorreosApi) : CorreosRepository {

    var cache: List<CorreosApiParcel>? = null

    override fun getParcelStatus(parcelId: String): Single<CorreosApiParcel> {
        return correosApi.getParcelStatus(parcelId)
                .map { element -> element.get(0) }
    }


    companion object {
        private var instance: CorreosRepositoryImpl? = null

        fun getInstance(correosApi: CorreosApi): CorreosRepositoryImpl {
            if (instance == null) {
                instance = CorreosRepositoryImpl(correosApi)
            }
            return instance!!
        }
    }

}