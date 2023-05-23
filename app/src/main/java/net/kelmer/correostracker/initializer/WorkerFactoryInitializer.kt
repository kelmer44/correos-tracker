package net.kelmer.correostracker.initializer

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import net.kelmer.correostracker.AppInitializer
import net.kelmer.correostracker.di.worker.MyWorkerFactory
import javax.inject.Inject

class WorkerFactoryInitializer @Inject constructor(private val myWorkerFactory: MyWorkerFactory) : AppInitializer {
    override fun initialize(application: Application) {
        WorkManager.initialize(
            application,
            Configuration.Builder()
                .setWorkerFactory(myWorkerFactory)
                .build()
        )
    }
}
