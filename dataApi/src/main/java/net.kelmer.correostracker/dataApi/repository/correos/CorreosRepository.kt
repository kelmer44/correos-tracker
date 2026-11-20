package net.kelmer.correostracker.dataApi.repository.correos

import io.reactivex.Single
import net.kelmer.correostracker.dataApi.model.remote.CorreosApiParcel

interface CorreosRepository {
    fun retrieveParcel(parcelCode: String): Single<CorreosApiParcel>
    /** [markSeen]: also record the latest event as notified (in-app refresh). The worker passes false. */
    fun getParcelStatus(parcelId: String, markSeen: Boolean = true): Single<CorreosApiParcel>
}
