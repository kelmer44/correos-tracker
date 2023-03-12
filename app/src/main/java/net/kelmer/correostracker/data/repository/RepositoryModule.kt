package net.kelmer.correostracker.data.repository

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.kelmer.correostracker.data.model.local.LocalParcelDao
import net.kelmer.correostracker.data.repository.correos.CorreosRepository
import net.kelmer.correostracker.data.repository.correos.CorreosRepositoryImpl
import net.kelmer.correostracker.data.repository.local.LocalParcelRepository
import net.kelmer.correostracker.data.repository.local.LocalParcelRepositoryImpl

/**
 * Created by gabriel on 25/03/2018.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    companion object {
        @JvmStatic
        @Provides
        fun provideLocalParcelRepository(localParcelDao: LocalParcelDao): net.kelmer.correostracker.data.repository.local.LocalParcelRepository {
            return LocalParcelRepositoryImpl.getInstance(localParcelDao)
        }
    }
    @Binds
    abstract fun bindsCorreosRepository(repo: CorreosRepositoryImpl): net.kelmer.correostracker.data.repository.correos.CorreosRepository
}
