package net.kelmer.correostracker.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.ListenableWorker
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import net.kelmer.correostracker.base.BaseViewModel
import net.kelmer.correostracker.data.Resource
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.data.model.remote.CorreosApiParcel
import net.kelmer.correostracker.data.network.exception.WrongCodeException
import net.kelmer.correostracker.data.repository.correos.CorreosRepository
import net.kelmer.correostracker.data.repository.local.LocalParcelRepository
import net.kelmer.correostracker.ext.withNetwork
import net.kelmer.correostracker.service.worker.ParcelPollWorker
import net.kelmer.correostracker.usecases.delete.DeleteParcelUseCase
import net.kelmer.correostracker.usecases.list.GetParcelListUseCase
import net.kelmer.correostracker.usecases.notifications.SwitchNotificationsUseCase
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by gabriel on 25/03/2018.
 */
class ParcelListViewModel @Inject constructor(
        private val getParcelListUseCase: GetParcelListUseCase,
        private val deleteParcelUseCase: DeleteParcelUseCase,
        private val switchNotificationsUseCase: SwitchNotificationsUseCase,
        private val parcelRepository: CorreosRepository)
    : BaseViewModel(
        getParcelListUseCase,
        deleteParcelUseCase,
        switchNotificationsUseCase) {

    private val _parcelList: MutableLiveData<Resource<List<LocalParcelReference>>> = MutableLiveData()
    fun getParcelList() = _parcelList
    fun retrieveParcelList() = getParcelListUseCase(Unit, _parcelList)

    private val _deleteLiveData: MutableLiveData<Resource<Unit>> = MutableLiveData()
    fun getDeleteResult() = _deleteLiveData
    fun deleteParcel(parcelReference: LocalParcelReference) {
        deleteParcelUseCase(DeleteParcelUseCase.Params(parcelReference), _deleteLiveData)
    }

    val statusReports: MutableLiveData<CorreosApiParcel> = MutableLiveData()
    fun refresh(items: List<LocalParcelReference>) {
        items.forEachIndexed { i, p ->
            parcelRepository.getParcelStatus(p.code)
                    .withNetwork(networkInteractor)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribeBy(onError = {
                        if(it is WrongCodeException){
                            Timber.w("Wrong code: ${p.code}!")
                        }
                        else {
                            Timber.e(it, "Could not update $i : ${it.message}")
                        }
                    },
                            onSuccess = {
                                statusReports.value = it
                            })
                    .addTo(disposables)
        }
    }


//    fun test() {
//        localParcelRepository.getParcels()
//                .firstOrError()
//                .flattenAsFlowable { it }
//                .flatMapSingle { local ->
//                    Timber.d("Parcel poll checking parcel with code ${local.code}")
//                    parcelRepository.getParcelStatus(local.code)
//                            .map {
//                                ParcelPollWorker.ParcelStatusComparator(local, it)
//                            }
//                            .onErrorReturn {
//                                ParcelPollWorker.ParcelStatusComparator(local, null)
//                            }
//                }
//                .toList()
//                .doOnSuccess {
//                    val newEvents = getNewEvents(it)
//                    Timber.d("Parcel poll got new Events: ${newEvents}")
//                    //                    buildNotification(newEvents)
//                }
//                .map {
//                    ListenableWorker.Result.success()
//                }
//                .onErrorReturn {
//                    Timber.e(it, "Failure on worker!")
//                    ListenableWorker.Result.failure()
//                }
//                .subscribeOn(schedulerProvider.io())
//                .observeOn(schedulerProvider.ui())
//                .subscribeBy(
//                        onError = {
//
//                        },
//                        onSuccess = {
//
//                        }
//                ).addTo(disposables)
//    }


//    private fun getNewEvents(it: MutableList<ParcelPollWorker.ParcelStatusComparator>): MutableList<ParcelPollWorker.NewEventInfo> {
//        val newEvents = mutableListOf<ParcelPollWorker.NewEventInfo>()
//        it.forEach {
//            val previousParcel = it.previous
//            val currentParcel = it.new
//            if (currentParcel != null) {
//                val ultimoEstado = previousParcel.ultimoEstado
//                val last = currentParcel.eventos?.last()
//                if ((ultimoEstado != null && last != null) && ultimoEstado != last) {
//                    newEvents.add(ParcelPollWorker.NewEventInfo(previousParcel.code, previousParcel.parcelName, ultimoEstado))
//                }
//            }
//        }
//        return newEvents
//    }

    fun enableNotifications(code: String): LiveData<Resource<String>> {
        return switchNotificationsUseCase(SwitchNotificationsUseCase.Params(code, true))
    }

    fun disableNotifications(code: String): LiveData<Resource<String>> {
        return switchNotificationsUseCase(SwitchNotificationsUseCase.Params(code, false))
    }


}