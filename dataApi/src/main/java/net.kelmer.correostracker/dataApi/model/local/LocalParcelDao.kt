package net.kelmer.correostracker.dataApi.model.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by gabriel on 25/03/2018.
 */
@Dao
interface LocalParcelDao {

    @Query("select * from LocalParcelReference ORDER BY parcelName ")
    fun getParcels(): Flowable<List<LocalParcelReference>>

    @Query("select * from LocalParcelReference where trackingCode = :code")
    fun getParcel(code: String): Flowable<LocalParcelReference>

    @Query("select * from LocalParcelReference where trackingCode = :code")
    fun getParcelSync(code: String): Single<LocalParcelReference>

    @Query("select * from LocalParcelReference where notify = 1 ORDER BY parcelName ")
    fun getNotifiableParcels(): Single<List<LocalParcelReference>>

    @Update
    fun updateParcel(parcel: LocalParcelReference): Completable

    @Query("update LocalParcelReference set notify = 0 where trackingCode = :code")
    fun disableNotifications(code: String): Completable

    @Query("update LocalParcelReference set notify = 1 where trackingCode = :code")
    fun enableNotifications(code: String): Completable

    @Insert(onConflict = REPLACE)
    fun saveParcel(parcel: LocalParcelReference): Long

    @Delete
    fun deleteParcel(parcel: LocalParcelReference): Completable

    @Query("select * from LocalParcelReference where trackingCode = :code")
    fun getParcelSequential(code: String): LocalParcelReference
}
