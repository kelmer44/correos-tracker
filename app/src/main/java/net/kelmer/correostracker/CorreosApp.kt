package net.kelmer.correostracker

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.facebook.stetho.Stetho
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import io.fabric.sdk.android.Fabric
import net.kelmer.correostracker.data.db.DbModule
import net.kelmer.correostracker.di.application.ApplicationComponent
import net.kelmer.correostracker.di.application.ApplicationModule
import net.kelmer.correostracker.di.application.DaggerApplicationComponent
import net.kelmer.correostracker.di.worker.MyWorkerFactory
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by gabriel on 25/03/2018.
 */
class CorreosApp : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject lateinit var myWorkerFactory: MyWorkerFactory



    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    companion object {
        lateinit var graph: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()
        DaggerApplicationComponent.factory().create(this).inject(this)
        initCrashlytics()
        setupTimber()
        setupStetho()
        setupWorkerFactory()
    }

    private fun setupWorkerFactory() {
        WorkManager.initialize(
                this,
                Configuration.Builder()
                        .setWorkerFactory(myWorkerFactory)
                        .build()
        )
    }

    private fun initCrashlytics() {

        Fabric.with(this, Crashlytics.Builder().core(CrashlyticsCore.Builder()
                .disabled(BuildConfig.DEBUG)
                .build()).build())
    }

    private fun setupTimber() {
        Timber.plant(Timber.DebugTree())
    }

    private fun setupStetho() {
        Stetho.initializeWithDefaults(this)
    }

}