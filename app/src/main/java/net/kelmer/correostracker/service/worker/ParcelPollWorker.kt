package net.kelmer.correostracker.service.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import net.kelmer.correostracker.data.repository.correos.CorreosRepository
import net.kelmer.correostracker.data.repository.local.LocalParcelRepository
import net.kelmer.correostracker.di.worker.ChildWorkerFactory
import javax.inject.Inject

class ParcelPollWorker @Inject constructor(val parcelRepository: LocalParcelRepository,
                                           val correosRepository: CorreosRepository,
                                           appContext: Context, workerParams: WorkerParameters): Worker(appContext, workerParams) {
    override fun doWork(): Result {



        return Result.success()
    }

    class Factory @Inject constructor(
            val myRepository: LocalParcelRepository,
            val networkService: CorreosRepository
            ): ChildWorkerFactory {

        override fun create(appContext: Context, params: WorkerParameters): Worker {
            return ParcelPollWorker(myRepository, networkService, appContext, params)
        }
    }



}