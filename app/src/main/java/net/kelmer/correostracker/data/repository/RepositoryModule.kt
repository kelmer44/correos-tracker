package net.kelmer.correostracker.data.repository

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.kelmer.correostracker.data.model.local.LocalParcelDao
import net.kelmer.correostracker.data.network.correos.CorreosApi
import net.kelmer.correostracker.data.repository.correos.CorreosRepository
import net.kelmer.correostracker.data.repository.correos.CorreosRepositoryImpl
import net.kelmer.correostracker.data.repository.local.LocalParcelRepository
import net.kelmer.correostracker.data.repository.local.LocalParcelRepositoryImpl
import javax.inject.Singleton

/**
 * Created by gabriel on 25/03/2018.
 */
@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideLocalParcelRepository(localParcelDao: LocalParcelDao): LocalParcelRepository {
        return LocalParcelRepositoryImpl.getInstance(localParcelDao)
    }
    @Provides
    @Singleton
    fun provideCorreosRepository(correosApi: CorreosApi, correosDao: LocalParcelDao): CorreosRepository {
        return CorreosRepositoryImpl.getInstance(correosApi, correosDao)
    }
}
