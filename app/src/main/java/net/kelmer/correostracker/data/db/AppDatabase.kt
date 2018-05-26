package net.kelmer.correostracker.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import net.kelmer.correostracker.data.model.local.LocalParcelDao
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.data.model.local.StanceConverter

/**
 * Created by gabriel on 25/03/2018.
 */
@Database(
        entities = [
        (LocalParcelReference::class)
        ],
        version = 4)
@TypeConverters(StanceConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun localParcelDao(): LocalParcelDao
}