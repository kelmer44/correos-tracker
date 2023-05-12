package net.kelmer.correostracker

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by gabriel on 25/03/2018.
 */
@HiltAndroidApp
class CorreosApp : Application(), Configuration.Provider {

    @Inject
    lateinit var initializers: Set<@JvmSuppressWildcards AppInitializer>

    @Inject
    @ProcessLifecycleObserver
    lateinit var lifecycleObservers: Set<@JvmSuppressWildcards LifecycleObserver>

    override fun onCreate() {
        super.onCreate()
        initializers.forEach {
            it.initialize(this)
        }
        lifecycleObservers.forEach {
            Timber.i("Adding lifecycleobserver: $it")
            ProcessLifecycleOwner.get().lifecycle.addObserver(it)
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
