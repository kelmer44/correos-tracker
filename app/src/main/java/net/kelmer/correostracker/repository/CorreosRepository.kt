package net.kelmer.correostracker.repository

import io.reactivex.Single
import net.kelmer.correostracker.data.model.remote.CorreosApiParcel

interface CorreosRepository{
    fun getParcelStatus(parcelId: String): Single<CorreosApiParcel>
}
