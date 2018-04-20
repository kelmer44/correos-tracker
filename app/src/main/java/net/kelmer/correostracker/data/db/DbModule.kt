package net.kelmer.correostracker.data.db

import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import net.kelmer.correostracker.data.model.local.LocalParcelDao
import javax.inject.Singleton
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.migration.Migration



/**
 * Created by gabriel on 25/03/2018.
 */


@Module
class DbModule(val context: Context) {

    companion object {

        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE LocalParcelReference ADD COLUMN stance INTEGER")
            }
        }


    }


    @Provides
    @Singleton
    fun provideAppDatabase() : AppDatabase {
        return Room.databaseBuilder(context,
                AppDatabase::class.java, "mycujoo-database")
                .addMigrations(MIGRATION_1_2)
                .build()
    }

    @Provides
    @Singleton
    fun provideLocalParcelDao(database: AppDatabase) : LocalParcelDao {
        return database.localParcelDao()
    }

}