package net.kelmer.correostracker.ui.activity

import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava2.subscribeAsState
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.android.billingclient.api.BillingClient.ProductType
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.QueryProductDetailsParams
import com.google.android.gms.ads.MobileAds
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDisposable
import dagger.hilt.android.AndroidEntryPoint
import net.kelmer.correostracker.ActivityLifecycleObserver
import net.kelmer.correostracker.CorreosApp
import net.kelmer.correostracker.R
import net.kelmer.correostracker.di.worker.MyWorkerFactory
import net.kelmer.correostracker.iap.IapApi
import net.kelmer.correostracker.iar.InAppReviewService
import net.kelmer.correostracker.service.worker.NotificationID
import net.kelmer.correostracker.service.worker.PERMISSION_NOTIS
import net.kelmer.correostracker.service.worker.ParcelPollWorker
import net.kelmer.correostracker.ui.CorreosComposeApp
import net.kelmer.correostracker.ui.theme.CorreosTheme
import net.kelmer.correostracker.ui.theme.ThemeMode
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Gabriel Sanmartín on 09/11/2020.
 */
@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @Inject
    lateinit var myWorkerFactory: MyWorkerFactory

    @Inject
    lateinit var inAppReviewService: InAppReviewService

    @Inject
    @ActivityLifecycleObserver
    lateinit var lifecycleObservers: Set<@JvmSuppressWildcards LifecycleObserver>

    @Inject
    lateinit var iapApi: IapApi

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this) {}

        lifecycleObservers.forEach {
            Timber.i("Adding lifecycleObserver $it")
            lifecycle.addObserver(it)
        }

        Timber.i("Recreating activity")
        setContent {

            val viewModel: MainActivityViewModel = hiltViewModel()
            val activityState by viewModel.stateOnceAndStream.subscribeAsState(MainActivityViewModel.State())

            val useDarkColors = when (activityState.theme) {
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
            }
            Timber.w("IsPremium = ${activityState.premiumState.isPremium}, billingAvailable = ${activityState.premiumState.isBillingAvailable}")
            val windowSizeClass = calculateWindowSizeClass(this)
            CorreosTheme(useDarkColors) {
                CorreosComposeApp(
                    premium = activityState.premiumState,
                    useDarkTheme = useDarkColors,
                    windowSizeClass = windowSizeClass,
                    onWebClicked = ::onWebClicked,
                    onBuyClicked = ::onBuyClicked
//                    displayFeatures
                )
            }
        }

        initWorker()
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(PERMISSION_NOTIS)) {
            ActivityCompat.requestPermissions(this, arrayOf(PERMISSION_NOTIS), NOTI_REQ_PERMISSION)
        }

        inAppReviewService.showIfNeeded()
    }

    private fun onBuyClicked() {
        iapApi.launchFlow(this)
            .autoDisposable(lifecycle.scope(Lifecycle.Event.ON_STOP))
            .subscribe({
                Timber.i("Successfully launched Purchase flow")
            }, Timber::e)
    }

    private fun onWebClicked() {
        val url = "https://github.com/kelmer44/correos-tracker"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    private fun triggerSampleNotification() {
        val notificationIntent = Intent(applicationContext, MainActivity::class.java)
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(
                applicationContext,
                0,
                notificationIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getActivity(applicationContext, 0, notificationIntent, 0)
        }

        val notification = NotificationCompat.Builder(applicationContext, ParcelPollWorker.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_reparto)
            .setContentTitle("Test")
            .setContentText("This is a test")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("This is a big test")
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(intent)
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(applicationContext).notify(NotificationID.id, notification)
        } catch (s: SecurityException) {
            Timber.e(s)
            FirebaseCrashlytics.getInstance().recordException(s)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == NOTI_REQ_PERMISSION) {
            Timber.i("Permission granted!")
        }
    }

    private fun initWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val uploadWorker = PeriodicWorkRequest
            .Builder(ParcelPollWorker::class.java, 15L, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                CorreosApp.PARCEL_CHECKER_WORKREQUEST,
                ExistingPeriodicWorkPolicy.REPLACE,
                uploadWorker
            )
    }

    companion object {
        const val NOTI_REQ_PERMISSION = 1
    }
}
