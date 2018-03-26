package net.kelmer.correostracker.data.repository.correos

import io.reactivex.Flowable
import net.kelmer.correostracker.data.model.remote.CorreosApiParcel

interface CorreosRepository{
    fun getParcelStatus(parcelId: String): Flowable<CorreosApiParcel>
}
