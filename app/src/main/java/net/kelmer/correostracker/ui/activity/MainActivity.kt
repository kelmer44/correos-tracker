package net.kelmer.correostracker.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
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
import net.kelmer.correostracker.service.worker.ParcelPollWorker
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
    }

    private fun initWorker() {
        val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        val uploadWorker = PeriodicWorkRequest.Builder(
                ParcelPollWorker::class.java, 15L, TimeUnit.MINUTES
        )
                .setConstraints(constraints)
                .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(CorreosApp.PARCEL_CHECKER_WORKREQUEST, ExistingPeriodicWorkPolicy.REPLACE, uploadWorker)

        parcelListPreferences.themeModeLive.observe(this, ThemeObserver())
    }


    inner class ThemeObserver : Observer<Int> {
        override fun onChanged(t: Int) {
            if (t != null) {
                AppCompatDelegate.setDefaultNightMode(t)
            }
        }
    }
}
