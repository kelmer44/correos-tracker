package net.kelmer.correostracker.data.model.local

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by gabriel on 25/03/2018.
 */
@Dao
interface LocalParcelDao {

    @Query("select * from LocalParcelReference ORDER BY parcelName ")
    fun getParcels() : Flowable<List<LocalParcelReference>>

    @Query("select * from LocalParcelReference where code = :code")
    fun getParcel(code: String) : Flowable<LocalParcelReference>

    @Query("select * from LocalParcelReference where code = :code")
    fun getParcelSync(code: String) : Single<LocalParcelReference>


    @Insert(onConflict = REPLACE)
    fun saveParcel(parcel: LocalParcelReference) : Long


    @Delete
    fun deleteParcel(parcel: LocalParcelReference) : Int
}