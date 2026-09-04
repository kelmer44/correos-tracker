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
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
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
import java.util.concurrent.atomic.AtomicBoolean
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

    private lateinit var consentInformation: ConsentInformation
    private var isMobileAdsInitializeCalled = AtomicBoolean(false)

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView
        super.onCreate(savedInstanceState)

        requestConsent()

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
                    onBuyClicked = ::onBuyClicked
                )
            }
        }

        initWorker()
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(
                PERMISSION_NOTIS
            )
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(PERMISSION_NOTIS), NOTI_REQ_PERMISSION)
        }

        inAppReviewService.showIfNeeded()
    }

    private fun requestConsent() {
        val params = ConsentRequestParameters
            .Builder()
            .build()
        ConsentDebugSettings
            .Builder(this)
            .addTestDeviceHashedId("8D36569186B06E4C7C60D856AC0E4A7B")

        consentInformation = UserMessagingPlatform.getConsentInformation(this)
        consentInformation.requestConsentInfoUpdate(this, params, {
            UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                this@MainActivity
            ) { loadAndShowError ->
                if (loadAndShowError != null) {
                    // Consent gathering failed.
                    Timber.w("${loadAndShowError.errorCode}: ${loadAndShowError.message}")
                }

                // Consent has been gathered.
                if (consentInformation.canRequestAds()) {
                    initializeMobileAdsSdk()
                }
            }

        }, { requestConsentError ->
            Timber.w("${requestConsentError.errorCode}: ${requestConsentError.message}")
        }
        )
        // Check if you can initialize the Google Mobile Ads SDK in parallel
        // while checking for new consent information. Consent obtained in
        // the previous session can be used to request ads.
        if (consentInformation.canRequestAds()) {
            initializeMobileAdsSdk()
        }
    }

    private fun initializeMobileAdsSdk() {
        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            return
        }

        // Initialize the Google Mobile Ads SDK.
        MobileAds.initialize(this) {}
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder().setTestDeviceIds(
                listOf("11FFB04655CCB851EBC6E015D5ECDB0F", "149B05EAFE1941F7B62AF10E6B45270A"),
            ).build()
        )
    }

    private fun onBuyClicked() {
        iapApi.launchFlow(this)
            .autoDisposable(lifecycle.scope(Lifecycle.Event.ON_STOP))
            .subscribe({
                Timber.i("Successfully launched Purchase flow")
            }, Timber::e)
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
            PendingIntent.getActivity(
                applicationContext,
                0,
                notificationIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
        }

        val notification =
            NotificationCompat.Builder(applicationContext, ParcelPollWorker.CHANNEL_ID)
                .setSmallIcon(net.kelmer.correostracker.theme.R.drawable.ic_reparto)
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
            NotificationManagerCompat.from(applicationContext)
                .notify(NotificationID.id, notification)
        } catch (s: SecurityException) {
            Timber.e(s)
            FirebaseCrashlytics.getInstance().recordException(s)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
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
