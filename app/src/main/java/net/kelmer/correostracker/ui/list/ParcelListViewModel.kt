package net.kelmer.correostracker.ui.list

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.MutableLiveData
import androidx.work.ListenableWorker
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import net.kelmer.correostracker.R
import net.kelmer.correostracker.base.BaseViewModel
import net.kelmer.correostracker.data.Result
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.data.model.remote.CorreosApiParcel
import net.kelmer.correostracker.data.repository.correos.CorreosRepository
import net.kelmer.correostracker.data.repository.local.LocalParcelRepository
import net.kelmer.correostracker.ext.toResult
import net.kelmer.correostracker.ext.withNetwork
import net.kelmer.correostracker.service.worker.NotificationID
import net.kelmer.correostracker.service.worker.ParcelPollWorker
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by gabriel on 25/03/2018.
 */
class ParcelListViewModel @Inject constructor(val localParcelRepository: LocalParcelRepository,
                                              val parcelRepository: CorreosRepository) : BaseViewModel() {

    val parcelList: MutableLiveData<Result<List<LocalParcelReference>>> = MutableLiveData()
    val deleteLiveData: MutableLiveData<Result<Int>> = MutableLiveData()


    fun retrieveParcelList() {
        localParcelRepository.getParcels()
                .toResult(schedulerProvider)
                .subscribeBy(
                        onNext = {
                            parcelList.value = it
                        }
                ).addTo(disposables)
    }

    fun deleteParcel(parcelReference: LocalParcelReference) {
        localParcelRepository.deleteParcel(parcelReference)
                .toResult(schedulerProvider)
                .subscribeBy(
                        onNext = {
                            deleteLiveData.value = it
                        }
                )
                .addTo(disposables)
    }


    val statusReports: MutableLiveData<CorreosApiParcel> = MutableLiveData()


    fun refresh(items: MutableList<LocalParcelReference>) {
        items.forEachIndexed { i, p ->
            parcelRepository.getParcelStatus(p.code)
                    .withNetwork(networkInteractor)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribeBy(onError = {
                        Timber.e(it, "Could not update $i : ${it.message}")
                    },
                            onSuccess = {
                                statusReports.value = it
                            })
                    .addTo(disposables)
        }
    }


    fun test() {
        localParcelRepository.getParcels()
                .firstOrError()
                .flattenAsFlowable { it }
                .flatMapSingle{ local ->
                    Timber.d("Parcel poll checking parcel with code ${local.code}")
                    parcelRepository.getParcelStatus(local.code)
                            .map {
                                ParcelPollWorker.ParcelStatusComparator(local, it)
                            }
                            .onErrorReturn {
                                ParcelPollWorker.ParcelStatusComparator(local, null)
                            }
                }
                .toList()
                .doOnSuccess {
                    val newEvents = getNewEvents(it)
                    Timber.d("Parcel poll got new Events: ${newEvents}")
//                    buildNotification(newEvents)
                }
                .map {
                    ListenableWorker.Result.success()
                }
                .onErrorReturn {
                    Timber.e(it, "Failure on worker!")
                    ListenableWorker.Result.failure()
                }
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeBy(
                        onError = {

                        },
                        onSuccess = {

                        }
                ).addTo(disposables)
    }


    private fun getNewEvents(it: MutableList<ParcelPollWorker.ParcelStatusComparator>): MutableList<ParcelPollWorker.NewEventInfo> {
        val newEvents = mutableListOf<ParcelPollWorker.NewEventInfo>()
        it.forEach {
            val previousParcel = it.previous
            val currentParcel = it.new
            if (currentParcel != null) {
                val ultimoEstado = previousParcel.ultimoEstado
                val last = currentParcel.eventos?.last()
                if ((ultimoEstado != null && last != null) && ultimoEstado != last) {
                    newEvents.add(ParcelPollWorker.NewEventInfo(previousParcel.code, previousParcel.parcelName, ultimoEstado))
                }
            }
        }
        return newEvents
    }


}