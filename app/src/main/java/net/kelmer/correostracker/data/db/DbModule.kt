package net.kelmer.correostracker.data.db

import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import net.kelmer.correostracker.data.model.local.LocalParcelDao
import javax.inject.Singleton

/**
 * Created by gabriel on 25/03/2018.
 */
@Module
class DbModule(val context: Context) {

    @Provides
    @Singleton
    fun provideAppDatabase() : AppDatabase {
        return Room.databaseBuilder(context,
                AppDatabase::class.java, "mycujoo-database").build()
    }

    @Provides
    @Singleton
    fun provideLocalParcelDao(database: AppDatabase) : LocalParcelDao {
        return database.localParcelDao()
    }

}