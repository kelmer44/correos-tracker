package net.kelmer.correostracker.ui.list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.work.*
import kotlinx.android.synthetic.main.activity_main.*
import net.kelmer.correostracker.R
import net.kelmer.correostracker.base.activity.BaseActivity
import net.kelmer.correostracker.data.prefs.SharedPrefsManager
import net.kelmer.correostracker.di.worker.MyWorkerFactory
import net.kelmer.correostracker.service.worker.ParcelPollWorker
import net.kelmer.correostracker.ui.create.CreateActivity
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ParcelListActivity : BaseActivity() {


    @Inject
    lateinit var myWorkerFactory: MyWorkerFactory


    @Inject
    lateinit var sharedPrefsManager: SharedPrefsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        fab.setOnClickListener {
            startActivityForResult(CreateActivity.newIntent(this),
                    REQ_CREATE_PARCEL)
        }

        val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()


        val uploadWorker = PeriodicWorkRequest.Builder(
                ParcelPollWorker::class.java, 15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(PARCEL_CHECKER_WORKREQUEST, ExistingPeriodicWorkPolicy.REPLACE, uploadWorker)

        sharedPrefsManager.nightModeLive.observe(this){
            delegate.localNightMode = it
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CREATE_PARCEL && resultCode == Activity.RESULT_OK) {
            for (fragment in supportFragmentManager.fragments) {
                fragment.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    companion object {
        const val REQ_CREATE_PARCEL = 101
        const val PARCEL_CHECKER_WORKREQUEST = "PARCEL-CHECKER"
    }
}
