package net.kelmer.correostracker.di.application

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import dagger.Module
import dagger.Provides
import net.kelmer.correostracker.CorreosApp
import net.kelmer.correostracker.util.AppSchedulerProvider
import net.kelmer.correostracker.util.SchedulerProvider
import javax.inject.Singleton

/**
 * Created by gabriel on 25/03/2018.
 */
@Module
class ApplicationModule(private val app: CorreosApp) {

    @Provides
    @Singleton
    fun provideApplication(): Application = app


    @Provides
    @Singleton
    fun provideContext(): Context = app.baseContext


    @Provides
    @Singleton
    fun provideResources(): Resources = app.resources

    @Provides
    @Singleton
    fun provideLayoutInflater(context: Context): LayoutInflater {
        return LayoutInflater.from(context)
    }

    @Singleton
    @Provides
    fun provideSchedulerProvider(): SchedulerProvider = AppSchedulerProvider()




}