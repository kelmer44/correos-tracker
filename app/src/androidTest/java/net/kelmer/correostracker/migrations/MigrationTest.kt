package net.kelmer.correostracker.migrations

import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.InstrumentationRegistry
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnit4
import net.kelmer.correostracker.data.local.AppDatabase
import net.kelmer.correostracker.data.local.DbModule
import net.kelmer.correostracker.dataApi.model.local.LocalParcelReference
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
@RunWith(AndroidJUnit4::class)
class MigrationTest {
    private val TEST_DB = "migration-test"

    @Rule
    @JvmField
    public val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    @Throws(IOException::class)
    fun migrate6To7() {

        var db = helper.createDatabase(TEST_DB, 6).apply {
            execSQL(
                "INSERT INTO LocalParcelReference" +
                    "(code, parcelName, stance, fecEvento,  horEvento, codEvento,fase, desTextoResumen, desTextoAmpliado, unidad," +
                    " lastChecked, largo, ancho, alto, peso, refCliente, codProducto, fechaCalculada, notify) " +
                    "VALUES " +
                    "('CODE','NAME', 1,'2020-01-02','22:00','P040000V','2', 'Clasificado','Envío clasificado en Centro Logístico', 'CTA SANTIAGO DE COMPOSTELA'," +
                    "123456789, '20','10','5','755','1234','PQ','2020-08-01 22:00', 1)"
            )
            close()
        }

        db = helper.runMigrationsAndValidate(TEST_DB, 7, true, DbModule.MIGRATION_6_7)

        val migratedRoomDatabase = getMigratedRoomDatabase()
        val parcel = migratedRoomDatabase.localParcelDao().getParcelSequential("MIGRATED_CODE")
        assertEquals("CODE", parcel.trackingCode)
        assertEquals("MIGRATED_CODE", parcel.code)
    }

    @Test
    @Throws(IOException::class)
    fun migrate7To8() {

        var db = helper.createDatabase(TEST_DB, 7).apply {
            execSQL(
                "INSERT INTO LocalParcelReference" +
                    "(code, trackingCode, parcelName, stance, fecEvento,  horEvento, codEvento,fase, desTextoResumen, desTextoAmpliado, unidad," +
                    " lastChecked, largo, ancho, alto, peso, refCliente, codProducto, fechaCalculada, notify) " +
                    "VALUES " +
                    "('CODE','TRACKINGCODE', 'NAME', 1,'2020-01-02','22:00','P040000V','2', 'Clasificado','Envío clasificado en Centro Logístico', 'CTA SANTIAGO DE COMPOSTELA'," +
                    "123456789, '20','10','5','755','1234','PQ','2020-08-01 22:00', 1)"
            )
            close()
        }

        db = helper.runMigrationsAndValidate(TEST_DB, 8, true, DbModule.MIGRATION_7_8)

        val migratedRoomDatabase = getMigratedRoomDatabase()
        val parcel = migratedRoomDatabase.localParcelDao().getParcelSequential("TRACKINGCODE")
        assertEquals(LocalParcelReference.UpdateStatus.UNKNOWN, parcel.updateStatus)
    }

    fun getMigratedRoomDatabase(): AppDatabase {
        val database = Room.databaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java, TEST_DB
        )
            .addMigrations(DbModule.MIGRATION_6_7)
            .build()
        // close the database and release any stream resources when the test finishes
        helper.closeWhenFinished(database)
        return database
    }
}
