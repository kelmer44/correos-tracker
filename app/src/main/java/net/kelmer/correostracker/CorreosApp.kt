package net.kelmer.correostracker

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.work.Configuration
import androidx.work.WorkManager
import com.facebook.stetho.Stetho
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import net.kelmer.correostracker.di.debug.LumberYard
import net.kelmer.correostracker.di.worker.MyWorkerFactory
import net.kelmer.correostracker.service.worker.ParcelPollWorker
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by gabriel on 25/03/2018.
 */
@HiltAndroidApp
class CorreosApp : Application() {


    @Inject
    lateinit var lumberYard : LumberYard

    @Inject
    lateinit var myWorkerFactory: MyWorkerFactory

    override fun onCreate() {
        super.onCreate()
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
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
    }

    private fun setupTimber() {
        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Timber.plant(lumberYard.tree())
        }

    }

    private fun setupStetho() {
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }

    companion object {
        const val REQ_CREATE_PARCEL = 101
        const val PARCEL_CHECKER_WORKREQUEST = "PARCEL-CHECKER"
    }

}