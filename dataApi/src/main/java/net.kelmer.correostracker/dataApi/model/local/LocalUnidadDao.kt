package net.kelmer.correostracker.dataApi.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Maybe

@Dao
interface LocalUnidadDao {

    @Query("select * from LocalUnidad where officeId = :officeId")
    fun getUnidad(officeId: String): Maybe<LocalUnidad>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveUnidad(unidad: LocalUnidad) : Completable
}
