package net.kelmer.correostracker.data.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.kelmer.correostracker.data.network.correos.CorreosV1
import net.kelmer.correostracker.data.network.correos.Unidades
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by gabriel on 25/03/2018.
 */
@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Singleton
    @Provides
    fun providesCorreosV1(retrofit: Retrofit) = retrofit.create(CorreosV1::class.java)

//    @Singleton
//    @Provides
//    fun providesUnidadService(@Named("unidadService") retrofit: Retrofit) = retrofit.create(Unidades::class.java)
}
