package net.kelmer.correostracker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import net.kelmer.correostracker.data.model.local.LocalParcelDao
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.data.model.local.StanceConverter
import net.kelmer.correostracker.data.model.local.UpdateStatusConverter

/**
 * Created by gabriel on 25/03/2018.
 */
@Database(
    entities = [
        (LocalParcelReference::class)
    ],
    version = 8
)
@TypeConverters(StanceConverter::class, UpdateStatusConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun localParcelDao(): LocalParcelDao
}
