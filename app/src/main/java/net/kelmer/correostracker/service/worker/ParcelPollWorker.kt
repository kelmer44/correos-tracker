package net.kelmer.correostracker.service.worker

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.ListenableWorker
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.reactivex.Single
import net.kelmer.correostracker.CorreosApp
import net.kelmer.correostracker.R
import net.kelmer.correostracker.dataApi.model.local.LocalParcelReference
import net.kelmer.correostracker.dataApi.model.remote.CorreosApiEvent
import net.kelmer.correostracker.dataApi.model.remote.CorreosApiParcel
import net.kelmer.correostracker.dataApi.repository.correos.CorreosRepository
import net.kelmer.correostracker.dataApi.repository.local.LocalParcelRepository
import net.kelmer.correostracker.di.worker.ChildWorkerFactory
import net.kelmer.correostracker.ui.activity.MainActivityCompose
import timber.log.Timber
import javax.inject.Inject

class ParcelPollWorker constructor(
    private val parcelRepository: LocalParcelRepository,
    private val correosRepository: CorreosRepository,
    appContext: Context,
    workerParams: WorkerParameters
) : RxWorker(appContext, workerParams) {

    companion object {
        val CHANNEL_ID = CorreosApp::class.java.simpleName + ".notification_status_changes"
    }

    init {
        Timber.d("Parcel poll creating instance! $this")
    }

    override fun createWork(): Single<Result> {
        Timber.w("Parcel poll worker $this here trying to do some work!")

        return parcelRepository.getNotifiableParcels()
            .flattenAsFlowable { it }
            .flatMapSingle { local ->
                Timber.d("Parcel poll checking parcel with code ${local.trackingCode}")
                correosRepository.getParcelStatus(local.trackingCode)
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
                Timber.d("Parcel poll got new Events: $newEvents")
                buildNotification(newEvents)
            }
            .map {
                Result.success()
            }
            .onErrorReturn {
                FirebaseCrashlytics.getInstance().recordException(it)
                Timber.e(it, "Failure on worker!")
                Result.failure()
            }
    }

    private fun buildNotification(newEvents: List<NewEventInfo>) {
        if (newEvents.isNotEmpty()) {
            val text = if (newEvents.size == 1) {
                applicationContext.getString(R.string.new_event_single, newEvents.first().nameEnvio, newEvents.first().event.desTextoResumen)
            } else {
                applicationContext.getString(R.string.new_events_multiple, newEvents.size)
            }

            val notificationIntent = Intent(applicationContext, MainActivityCompose::class.java)

            notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

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

            val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_reparto)
                .setContentTitle(applicationContext.getString(R.string.notification_title))
                .setContentText(text)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(text)
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(intent)
                .setAutoCancel(true)
                .build()

            with(NotificationManagerCompat.from(applicationContext)) {
                // notificationId is a unique int for each notification that you must define
                val permissionCheck = NotificationManagerCompat.from(applicationContext).areNotificationsEnabled()

                if(permissionCheck) {
                    try {
                        notify(NotificationID.id, notification)
                    } catch (s: SecurityException){
                        Timber.e(s)
                        FirebaseCrashlytics.getInstance().recordException(s)
                    }
                }
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
                val last = currentParcel.eventos?.lastOrNull()
                if ((ultimoEstado != null && last != null) && ultimoEstado != last) {
                    newEvents.add(NewEventInfo(previousParcel.trackingCode, previousParcel.parcelName, last))
                }
            }
        }
        return newEvents
    }

    class Factory @Inject constructor(
        val myRepository: net.kelmer.correostracker.dataApi.repository.local.LocalParcelRepository,
        val networkService: net.kelmer.correostracker.dataApi.repository.correos.CorreosRepository
    ) : ChildWorkerFactory {

        override fun create(appContext: Context, params: WorkerParameters): ListenableWorker {
            return ParcelPollWorker(myRepository, networkService, appContext, params)
        }
    }

    data class ParcelStatusComparator(val previous: LocalParcelReference, val new: CorreosApiParcel?)
    data class NewEventInfo(val codEnvio: String, val nameEnvio: String, val event: CorreosApiEvent)
}
