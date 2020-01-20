package net.kelmer.correostracker.service.worker

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.ListenableWorker
import androidx.work.RxWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import net.kelmer.correostracker.CorreosApp
import net.kelmer.correostracker.R
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.data.model.remote.CorreosApiEvent
import net.kelmer.correostracker.data.model.remote.CorreosApiParcel
import net.kelmer.correostracker.data.repository.correos.CorreosRepository
import net.kelmer.correostracker.data.repository.local.LocalParcelRepository
import net.kelmer.correostracker.di.qualifiers.ForApplication
import net.kelmer.correostracker.di.worker.ChildWorkerFactory
import okhttp3.internal.notify
import timber.log.Timber
import javax.inject.Inject

class ParcelPollWorker constructor(val parcelRepository: LocalParcelRepository,
                                   val correosRepository: CorreosRepository,
                                   appContext: Context, workerParams: WorkerParameters) : RxWorker(appContext, workerParams) {


    companion object {
        val CHANNEL_ID = CorreosApp::class.java.simpleName + ".notification_status_changes"
    }


    init {
        Timber.d("Parcel poll creating instance! $this")
    }

    override fun createWork(): Single<Result> {
        Timber.w("Parcel poll worker $this here trying to do some work!")
        return parcelRepository.getParcelsSingle()
                .flattenAsFlowable { it  }
                .flatMapSingle { local ->
                    Timber.d("Parcel poll checking parcel with code ${local.code}")
                    correosRepository.getParcelStatus(local.code)
                            .map {
                                ParcelStatusComparator(local, it)
                            }
                            .onErrorReturn {
                                ParcelStatusComparator(local, null)
                            }
                            .doOnError {
                                Timber.w("Error emitting from innger single")
                            }
                            .doOnSuccess {
                                Timber.i("Success emitting from inner single")
                            }
                }
                .toList()
                .doOnSuccess {
                    val newEvents = getNewEvents(it)
                    Timber.d("Parcel poll got new Events: ${newEvents}")
                    buildNotification(newEvents)
                }
                .map {
                    Result.success()
                }
                .onErrorReturn {
                    Timber.e(it, "Failure on worker!")
                    Result.failure()
                }
    }

    private fun buildNotification(newEvents: List<NewEventInfo>) {
        if (newEvents.isNotEmpty()) {
            val text = if (newEvents.size == 1) {
                applicationContext.getString(R.string.new_event_single, newEvents.first().nameEnvio)
            } else {
                applicationContext.getString(R.string.new_events_multiple, newEvents.size)
            }

            var notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_delivery)
                    .setContentTitle(applicationContext.getString(R.string.notification_title))
                    .setContentText(text)
                    .setStyle(NotificationCompat.BigTextStyle()
                            .bigText(text))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT).build()
            with(NotificationManagerCompat.from(applicationContext)) {
                // notificationId is a unique int for each notification that you must define
                notify(NotificationID.id, notification)
            }
        }
    }

    private fun getNewEvents(it: MutableList<ParcelStatusComparator>): MutableList<NewEventInfo> {
        val newEvents = mutableListOf<NewEventInfo>()
        it.forEach {
            val previousParcel = it.previous
            val currentParcel = it.new
            if (currentParcel != null) {
                val ultimoEstado = previousParcel.ultimoEstado
                val last = currentParcel.eventos?.last()
                if ((ultimoEstado != null) && ultimoEstado != last) {
                    newEvents.add(NewEventInfo(previousParcel.code, previousParcel.parcelName, ultimoEstado))
                }
            }
        }
        return newEvents
    }


    class Factory @Inject constructor(
            val myRepository: LocalParcelRepository,
            val networkService: CorreosRepository
    ) : ChildWorkerFactory {

        override fun create(appContext: Context, params: WorkerParameters): ListenableWorker {
            return ParcelPollWorker(myRepository, networkService, appContext, params)
        }
    }


    data class ParcelStatusComparator(val previous: LocalParcelReference, val new: CorreosApiParcel?)
    data class NewEventInfo(val codEnvio: String, val nameEnvio: String, val event: CorreosApiEvent)


}