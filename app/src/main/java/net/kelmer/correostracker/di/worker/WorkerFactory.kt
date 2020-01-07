package net.kelmer.correostracker.di.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import javax.inject.Inject
import javax.inject.Provider

class MyWorkerFactory @Inject constructor(
        val workers: Map<Class<out Worker>, @JvmSuppressWildcards Provider<Worker>>
) : WorkerFactory() {

    override fun createWorker(
            appContext: Context,
            workerClassName: String,
            workerParameters: WorkerParameters
    ): ListenableWorker? {

        val workerFactoryProvider =

        val myWorker = //Iterate over available workers and instantiate
                return workerFactoryProvider.get().create(appContext, workerParameters)
    }
}