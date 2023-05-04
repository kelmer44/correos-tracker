package net.kelmer.correostracker.initializer

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics
import net.kelmer.correostracker.BuildConfig
import javax.inject.Inject

class CrashlyticsInitializer @Inject constructor(): Initializer {
    override fun initialize(application: Application) {
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
    }
}
