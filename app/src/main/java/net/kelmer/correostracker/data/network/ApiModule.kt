package net.kelmer.correostracker.data.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.kelmer.correostracker.data.network.correos.CorreosApi
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by gabriel on 25/03/2018.
 */
@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Singleton
    @Provides
    fun providesCorreosApi(retrofit: Retrofit) = retrofit.create(CorreosApi::class.java)
}
