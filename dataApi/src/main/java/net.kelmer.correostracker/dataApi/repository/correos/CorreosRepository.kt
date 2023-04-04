package net.kelmer.correostracker.dataApi.repository.correos

import io.reactivex.Single
import net.kelmer.correostracker.dataApi.model.remote.CorreosApiParcel

interface CorreosRepository {
    fun retrieveParcel(parcelCode: String): Single<CorreosApiParcel>
    fun getParcelStatus(parcelId: String): Single<CorreosApiParcel>
}
