package net.kelmer.correostracker.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import net.kelmer.correostracker.di.qualifiers.NetworkLogger
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

/**
 * Created by gabriel on 25/03/2018.
 */
@Module
@InstallIn(SingletonComponent::class)
open class Interceptors {

    @NetworkLogger
    @Singleton
    @Provides
    @IntoSet
    fun provideNetworkLogger(): Interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
}
