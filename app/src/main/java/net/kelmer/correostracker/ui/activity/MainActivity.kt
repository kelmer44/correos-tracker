package net.kelmer.correostracker.ui.activity

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Observer
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint
import net.kelmer.correostracker.BuildConfig
import net.kelmer.correostracker.CorreosApp
import net.kelmer.correostracker.R
import net.kelmer.correostracker.base.activity.BaseActivity
import net.kelmer.correostracker.di.worker.MyWorkerFactory
import net.kelmer.correostracker.list.ParcelListPreferences
import net.kelmer.correostracker.service.worker.NotificationID
import net.kelmer.correostracker.service.worker.PERMISSION_NOTIS
import net.kelmer.correostracker.service.worker.ParcelPollWorker
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Gabriel Sanmartín on 09/11/2020.
 */
@AndroidEntryPoint
class MainActivity : BaseActivity(R.layout.activity_main) {

    @Inject
    lateinit var myWorkerFactory: MyWorkerFactory

    @Inject
    lateinit var parcelListPreferences: ParcelListPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initWorker()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            shouldShowRequestPermissionRationale(PERMISSION_NOTIS)
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(PERMISSION_NOTIS), NOTI_REQ_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == NOTI_REQ_PERMISSION){
            Timber.i("Permission granted!")
        }
    }

    private fun initWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val uploadWorker = PeriodicWorkRequest
            .Builder(
                ParcelPollWorker::class.java, 15L, TimeUnit.MINUTES
            )
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                CorreosApp.PARCEL_CHECKER_WORKREQUEST,
                ExistingPeriodicWorkPolicy.REPLACE,
                uploadWorker
            )

        parcelListPreferences.themeModeLive.observe(this, ThemeObserver())
    }


    inner class ThemeObserver : Observer<Int> {
        override fun onChanged(t: Int) {
            if (t != null) {
                AppCompatDelegate.setDefaultNightMode(t)
            }
        }
    }

    companion object {
        const val NOTI_REQ_PERMISSION = 1
    }
}
