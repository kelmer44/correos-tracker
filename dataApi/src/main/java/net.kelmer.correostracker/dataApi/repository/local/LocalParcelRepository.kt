package net.kelmer.correostracker.dataApi.repository.local

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import net.kelmer.correostracker.dataApi.model.local.LocalParcelReference
import net.kelmer.correostracker.dataApi.model.local.SortOption

/**
 * Created by gabriel on 25/03/2018.
 */
interface LocalParcelRepository {
    fun getParcels(sortOption: SortOption = SortOption.DATE_ADDED): Flowable<List<LocalParcelReference>>
    fun getNotifiableParcels(): Single<List<LocalParcelReference>>
    fun getParcel(code: String): Flowable<LocalParcelReference>
    fun saveParcel(parcel: LocalParcelReference): Completable
    fun deleteParcel(parcel: LocalParcelReference): Completable
    fun setNotify(code: String, enable: Boolean): Completable
}
