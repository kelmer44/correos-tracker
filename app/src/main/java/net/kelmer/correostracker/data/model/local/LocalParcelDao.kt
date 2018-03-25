package net.kelmer.correostracker.data.model.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import io.reactivex.Single

/**
 * Created by gabriel on 25/03/2018.
 */
@Dao
interface LocalParcelDao {

    @Query("select * from LocalParcelReference")
    fun getParcels() : Single<List<LocalParcelReference>>

    @Query("select * from LocalParcelReference where code = :code")
    fun getParcel(code: String) : Single<LocalParcelReference>

    @Insert(onConflict = REPLACE)
    fun saveParcel(parcel: LocalParcelReference)

    @Delete
    fun deleteParcel(parcel: LocalParcelReference) : Int
}