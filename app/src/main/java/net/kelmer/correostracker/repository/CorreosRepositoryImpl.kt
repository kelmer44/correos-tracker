package net.kelmer.correostracker.repository

import io.reactivex.Single
import net.kelmer.correostracker.data.model.CorreosApiParcel
import net.kelmer.correostracker.data.network.ApiFactory
import net.kelmer.correostracker.data.network.CorreosApi

class CorreosRepositoryImpl(apiFactory: ApiFactory) : CorreosRepository {

    val api: CorreosApi = apiFactory.provideCorreosApi()
    var cache: List<CorreosApiParcel>? = null

    override fun getParcelStatus(parcelId: String): Single<CorreosApiParcel> {
        return api.getParcelStatus(parcelId)
                .map { element -> element.get(0) }
    }
}