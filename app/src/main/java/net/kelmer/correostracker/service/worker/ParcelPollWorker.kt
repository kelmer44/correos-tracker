package net.kelmer.correostracker.service.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.RxWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import net.kelmer.correostracker.data.repository.correos.CorreosRepository
import net.kelmer.correostracker.data.repository.local.LocalParcelRepository
import net.kelmer.correostracker.di.qualifiers.ForApplication
import net.kelmer.correostracker.di.worker.ChildWorkerFactory
import timber.log.Timber
import javax.inject.Inject

class ParcelPollWorker constructor(val parcelRepository: LocalParcelRepository,
                                   val correosRepository: CorreosRepository,
                                   appContext: Context, workerParams: WorkerParameters) : RxWorker(appContext, workerParams) {
    override fun createWork(): Single<Result> {
        return Flowable.range(0, 1)
                .flatMap {
                    parcelRepository.getParcels()
                }
                .flatMapIterable {
                    it
                }
                .map {
                    Timber.d("Parcel poll checking parcel with code ${ it.parcelName }}")
                    correosRepository.getParcelStatus(it.code)
                }
                .toList()
                .map { Result.success() }
                .onErrorReturn {
                    Result.failure()
                }
    }


    class Factory @Inject constructor(
            val myRepository: LocalParcelRepository,
            val networkService: CorreosRepository
    ) : ChildWorkerFactory {

        override fun create(appContext: Context, params: WorkerParameters): ListenableWorker {
            return ParcelPollWorker(myRepository, networkService, appContext, params)
        }
    }


}