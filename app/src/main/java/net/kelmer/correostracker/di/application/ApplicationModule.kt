package net.kelmer.correostracker.di.application

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.net.ConnectivityManager
import android.view.LayoutInflater
import dagger.Module
import dagger.Provides
import net.kelmer.correostracker.BuildConfig
import net.kelmer.correostracker.CorreosApp
import net.kelmer.correostracker.data.prefs.SharedPrefsManager
import net.kelmer.correostracker.data.prefs.SharedPrefsManagerImpl
import net.kelmer.correostracker.di.qualifiers.ForApplication
import net.kelmer.correostracker.util.AppSchedulerProvider
import net.kelmer.correostracker.util.NetworkInteractor
import net.kelmer.correostracker.util.NetworkInteractorImpl
import net.kelmer.correostracker.util.SchedulerProvider
import javax.inject.Singleton

/**
 * Created by gabriel on 25/03/2018.
 */
@Module
class ApplicationModule {


    @Provides
    @ForApplication
    fun provideAppContext(application: Application): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideLayoutInflater(@ForApplication context: Context): LayoutInflater {
        return LayoutInflater.from(context)
    }

    @Singleton
    @Provides
    fun provideSchedulerProvider(): SchedulerProvider = AppSchedulerProvider()


    @Provides
    @Singleton
    fun provideConnectivityManager(@ForApplication context: Context): ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    @Singleton
    fun provideNetworkInteractor(networkInteractor: NetworkInteractorImpl): NetworkInteractor = networkInteractor


    @Provides
    @Singleton
    fun provideSharedPreferences(@ForApplication app: Context): SharedPreferences {
        val sharedPrefsId  = "SEG_CORREOS_" + if (BuildConfig.DEBUG) "_DEBUG" else ""
        return app.getSharedPreferences(sharedPrefsId, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideSharedPreferencesManager(sharedPreferences: SharedPreferences) : SharedPrefsManager {
        return SharedPrefsManagerImpl(sharedPreferences)
    }

}

@Module
abstract class ApplicationModuleBinds {

}