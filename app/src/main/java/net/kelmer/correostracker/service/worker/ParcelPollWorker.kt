package net.kelmer.correostracker.service.worker

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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
import net.kelmer.correostracker.ui.activity.MainActivity
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
            .filter { it.ultimoEstado?.isEntregado() != true }
            .flatMapSingle { local ->
                Timber.d("Parcel poll checking parcel with code ${local.trackingCode}")
                // Recorded as notified only after the notification is posted (see below)
                correosRepository.getParcelStatus(local.trackingCode, markSeen = false)
                    .map {
                        ParcelStatusComparator(local, it)
                    }
                    .onErrorReturn {
                        ParcelStatusComparator(local, null)
                    }
            }
            .toList()
            .doOnSuccess {
                val newEvents = getNewEvents(it)
                Timber.d("Parcel poll got new Events: $newEvents")
                val handled = buildNotification(newEvents)
                recordNotifiedEvents(it, handled)
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

    /** @return false if posting failed, so the events are retried next run. */
    private fun buildNotification(newEvents: List<NewEventInfo>): Boolean {
        var handled = true
        if (newEvents.isNotEmpty()) {
            val text = if (newEvents.size == 1) {
                applicationContext.getString(R.string.new_event_single, newEvents.first().nameEnvio, newEvents.first().event.desTextoResumen)
            } else {
                applicationContext.getString(R.string.new_events_multiple, newEvents.size)
            }

            val notificationIntent = Intent(applicationContext, MainActivity::class.java)

            notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

            // FLAG_IMMUTABLE is a compile-time constant, so it is safe to pass below minSdk 23
            // (older platforms just ignore it). targeting SDK 31+ makes omitting it a fatal lint
            // error, and MainActivity already passes it unconditionally.
            val intent = PendingIntent.getActivity(
                applicationContext,
                0,
                notificationIntent,
                PendingIntent.FLAG_IMMUTABLE
            )

            val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(net.kelmer.correostracker.theme.R.drawable.ic_reparto)
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
                        handled = false
                        Timber.e(s)
                        FirebaseCrashlytics.getInstance().recordException(s)
                    }
                }
            }
        }
        return handled
    }

    /** Latest event the user knows about; falls back to ultimoEstado for older rows. */
    private fun knownEventKey(c: ParcelStatusComparator): String? =
        c.previous.lastNotifiedEventKey ?: c.previous.ultimoEstado?.notificationKey()

    private fun getNewEvents(it: MutableList<ParcelStatusComparator>): MutableList<NewEventInfo> {
        val newEvents = mutableListOf<NewEventInfo>()
        it.forEach {
            val previousParcel = it.previous
            val last = it.new?.eventos?.lastOrNull()
            val known = knownEventKey(it)
            if (last != null && known != null && known != last.notificationKey()) {
                newEvents.add(NewEventInfo(previousParcel.trackingCode, previousParcel.parcelName, last))
            }
        }
        return newEvents
    }

    /** Marks latest events as notified; new events only if [handled]. */
    private fun recordNotifiedEvents(comparators: List<ParcelStatusComparator>, handled: Boolean) {
        comparators.forEach { c ->
            val key = c.new?.eventos?.lastOrNull()?.notificationKey() ?: return@forEach
            val known = knownEventKey(c)
            val isNewEvent = known != null && known != key
            if (isNewEvent && !handled) return@forEach
            if (c.previous.lastNotifiedEventKey != key) {
                parcelRepository.setLastNotifiedEventKey(c.previous.code, key).blockingAwait()
            }
        }
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
