package net.kelmer.correostracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import net.kelmer.correostracker.dataApi.model.local.LocalParcelDao
import net.kelmer.correostracker.dataApi.model.local.LocalParcelReference
import net.kelmer.correostracker.dataApi.model.local.LocalUnidad
import net.kelmer.correostracker.dataApi.model.local.LocalUnidadDao
import net.kelmer.correostracker.dataApi.model.local.StanceConverter
import net.kelmer.correostracker.dataApi.model.local.UpdateStatusConverter
import net.kelmer.correostracker.dataApi.model.remote.unidad.Unidad

/**
 * Created by gabriel on 25/03/2018.
 */
@Database(
    entities = [
        LocalParcelReference::class,
        LocalUnidad::class
    ],
    version = 9
)
@TypeConverters(StanceConverter::class, UpdateStatusConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun localParcelDao(): LocalParcelDao
    abstract fun localUnidadDao(): LocalUnidadDao
}
