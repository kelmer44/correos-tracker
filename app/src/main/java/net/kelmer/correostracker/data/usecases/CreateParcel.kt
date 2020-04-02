package net.kelmer.correostracker.data.usecases

import io.reactivex.Single
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.data.model.remote.CorreosApiParcel

interface CreateParcel {
    fun execute(localParcelReference: LocalParcelReference) : Single<CorreosApiParcel>
}