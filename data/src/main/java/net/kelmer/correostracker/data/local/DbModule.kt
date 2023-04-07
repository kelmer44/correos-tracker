package net.kelmer.correostracker.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.kelmer.correostracker.dataApi.model.local.LocalParcelDao
import net.kelmer.correostracker.dataApi.model.local.LocalUnidadDao
import javax.inject.Singleton

/**
 * Created by gabriel on 25/03/2018.
 */
@Module
@InstallIn(SingletonComponent::class)
class DbModule {

    companion object {

        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE LocalParcelReference ADD COLUMN stance INTEGER")
            }
        }

        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE LocalParcelReference ADD COLUMN fecEvento TEXT")
                database.execSQL("ALTER TABLE LocalParcelReference ADD COLUMN codEvento TEXT")
                database.execSQL("ALTER TABLE LocalParcelReference ADD COLUMN horEvento TEXT")
                database.execSQL("ALTER TABLE LocalParcelReference ADD COLUMN fase TEXT")
                database.execSQL("ALTER TABLE LocalParcelReference ADD COLUMN desTextoResumen TEXT")
                database.execSQL("ALTER TABLE LocalParcelReference ADD COLUMN desTextoAmpliado TEXT")
                database.execSQL("ALTER TABLE LocalParcelReference ADD COLUMN unidad TEXT")
            }
        }

        val MIGRATION_3_4: Migration = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE LocalParcelReference ADD COLUMN lastChecked INTEGER")
            }
        }

        val MIGRATION_4_5: Migration = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE LocalParcelReference ADD COLUMN largo TEXT")
                database.execSQL("ALTER TABLE LocalParcelReference ADD COLUMN ancho TEXT")
                database.execSQL("ALTER TABLE LocalParcelReference ADD COLUMN alto TEXT")
                database.execSQL("ALTER TABLE LocalParcelReference ADD COLUMN peso TEXT")
                database.execSQL("ALTER TABLE LocalParcelReference ADD COLUMN refCliente TEXT")
                database.execSQL("ALTER TABLE LocalParcelReference ADD COLUMN codProducto TEXT")
                database.execSQL("ALTER TABLE LocalParcelReference ADD COLUMN fechaCalculada TEXT")
            }
        }

        val MIGRATION_5_6: Migration = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE LocalParcelReference ADD COLUMN notify INTEGER NOT NULL DEFAULT 1")
            }
        }
        val MIGRATION_6_7: Migration = object : Migration(6, 7) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE LocalParcelReference ADD COLUMN trackingCode TEXT NOT NULL DEFAULT ''")
                database.execSQL("UPDATE LocalParcelReference SET trackingCode = code")
                database.execSQL("UPDATE LocalParcelReference SET code = 'MIGRATED_' || code")
            }
        }

        val MIGRATION_7_8: Migration = object : Migration(7, 8) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE LocalParcelReference ADD COLUMN updateStatus INTEGER NOT NULL DEFAULT 0")
            }
        }

        val MIGRATION_8_9: Migration = object : Migration(8, 9) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE LocalUnidad (" +
                    "officeId TEXT PRIMARY KEY NOT NULL," +
                    "officeType TEXT DEFAULT NULL," +
                    "cityName TEXT DEFAULT NULL" +
                    ")")
            }
        }
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "mycujoo-database"
        )
            .addMigrations(
                MIGRATION_1_2,
                MIGRATION_2_3,
                MIGRATION_3_4,
                MIGRATION_4_5,
                MIGRATION_5_6,
                MIGRATION_6_7,
                MIGRATION_7_8,
                MIGRATION_8_9
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideLocalParcelDao(database: AppDatabase): LocalParcelDao {
        return database.localParcelDao()
    }

    @Provides
    @Singleton
    fun provideUnidadDao(database: AppDatabase): LocalUnidadDao {
        return database.localUnidadDao()
    }
}