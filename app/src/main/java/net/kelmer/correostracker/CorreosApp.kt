package net.kelmer.correostracker

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.work.Configuration
import androidx.work.WorkManager
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import net.kelmer.correostracker.di.debug.LumberYard
import net.kelmer.correostracker.di.worker.MyWorkerFactory
import net.kelmer.correostracker.initializer.Initializer
import net.kelmer.correostracker.service.worker.ParcelPollWorker
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by gabriel on 25/03/2018.
 */
@HiltAndroidApp
class CorreosApp : Application(), Configuration.Provider {

    @Inject
    lateinit var initializers: Set<@JvmSuppressWildcards Initializer>

    override fun onCreate() {
        super.onCreate()
        initializers.forEach {
            it.initialize(this)
        }
    }

    private fun createNotificationChannel() {

    }

    companion object {
        const val PARCEL_CHECKER_WORKREQUEST = "PARCEL-CHECKER"
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .build()
    }
}
