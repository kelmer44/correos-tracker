package net.kelmer.correostracker.di.application

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.kelmer.correostracker.BuildConfig
import net.kelmer.correostracker.BuildInfo
import net.kelmer.correostracker.di.BuildInfoImpl
import net.kelmer.correostracker.util.AppSchedulerProvider
import net.kelmer.correostracker.util.NetworkInteractor
import net.kelmer.correostracker.util.NetworkInteractorImpl
import net.kelmer.correostracker.util.SchedulerProvider
import javax.inject.Singleton

/**
 * Created by gabriel on 25/03/2018.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class ApplicationModule {

    @Binds
    abstract fun bindsBuildInfo(info: BuildInfoImpl): BuildInfo

    @Binds
    @Singleton
    abstract fun bindsScheduleProvider(impl: AppSchedulerProvider): SchedulerProvider

    @Binds
    @Singleton
    abstract fun bindsNetworkInteractor(impl: NetworkInteractorImpl): NetworkInteractor

    @Module
    @InstallIn(SingletonComponent::class)
    object ProvidesModule {

        @JvmStatic
        @Provides
        @Singleton
        fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        @JvmStatic
        @Provides
        @Singleton
        fun provideSharedPreferences(@ApplicationContext app: Context): SharedPreferences {
            val sharedPrefsId = "SEG_CORREOS_" + if (BuildConfig.DEBUG) "_DEBUG" else ""
            return app.getSharedPreferences(sharedPrefsId, Context.MODE_PRIVATE)
        }
    }
}
