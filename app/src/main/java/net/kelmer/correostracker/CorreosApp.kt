package net.kelmer.correostracker

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
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
import net.kelmer.correostracker.service.worker.ParcelPollWorker
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
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(ParcelPollWorker.CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
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