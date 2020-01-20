package net.kelmer.correostracker.data.model.local

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by gabriel on 25/03/2018.
 */
@Dao
interface LocalParcelDao {

    @Query("select * from LocalParcelReference ORDER BY parcelName ")
    fun getParcels(): Flowable<List<LocalParcelReference>>

    @Query("select * from LocalParcelReference where code = :code")
    fun getParcel(code: String): Flowable<LocalParcelReference>

    @Query("select * from LocalParcelReference where code = :code")
    fun getParcelSync(code: String): Single<LocalParcelReference>

    @Query("select * from LocalParcelReference where notify = 1 ORDER BY parcelName ")
    fun getNotifiableParcels(): Single<List<LocalParcelReference>>

    @Query("update LocalParcelReference set notify = 0 where code = :code")
    fun disableNotifications(code: String)

    @Query("update LocalParcelReference set notify = 1 where code = :code")
    fun enableNotifications(code: String)

    @Insert(onConflict = REPLACE)
    fun saveParcel(parcel: LocalParcelReference): Long

    @Delete
    fun deleteParcel(parcel: LocalParcelReference): Int
}