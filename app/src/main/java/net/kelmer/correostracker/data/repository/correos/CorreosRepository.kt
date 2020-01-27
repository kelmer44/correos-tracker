package net.kelmer.correostracker.data.repository.correos

import io.reactivex.Flowable
import io.reactivex.Single
import net.kelmer.correostracker.data.model.remote.CorreosApiParcel

interface CorreosRepository{
    fun getParcelStatus(parcelId: String): Single<CorreosApiParcel>
}
