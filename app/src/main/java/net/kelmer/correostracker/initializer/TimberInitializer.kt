package net.kelmer.correostracker.initializer

import android.app.Application
import net.kelmer.correostracker.BuildConfig
import net.kelmer.correostracker.di.debug.LumberYard
import timber.log.Timber
import javax.inject.Inject

class TimberInitializer @Inject constructor(
    private val lumberYard: LumberYard
) : Initializer {
    override fun initialize(application: Application) {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Timber.plant(lumberYard.tree())
        }
    }
}
