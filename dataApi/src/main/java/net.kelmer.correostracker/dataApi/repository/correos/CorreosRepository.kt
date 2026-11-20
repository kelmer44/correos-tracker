package net.kelmer.correostracker.dataApi.repository.correos

import io.reactivex.Single
import net.kelmer.correostracker.dataApi.model.remote.CorreosApiParcel

interface CorreosRepository {
    fun retrieveParcel(parcelCode: String): Single<CorreosApiParcel>
    /**
     * Fetches the status and stores it locally. With [markSeen] (the default, for in-app
     * refreshes) the latest event is also recorded as already notified, since the user is looking
     * at it. The background worker passes false and records it itself once notified.
     */
    fun getParcelStatus(parcelId: String, markSeen: Boolean = true): Single<CorreosApiParcel>
}
