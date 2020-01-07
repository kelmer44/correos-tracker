package net.kelmer.correostracker.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.work.ListenableWorker
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import net.kelmer.correostracker.base.BaseViewModel
import net.kelmer.correostracker.data.Result
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.data.model.remote.CorreosApiParcel
import net.kelmer.correostracker.data.repository.correos.CorreosRepository
import net.kelmer.correostracker.data.repository.local.LocalParcelRepository
import net.kelmer.correostracker.ext.toResult
import net.kelmer.correostracker.ext.withNetwork
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
//        items.forEachIndexed { i, p ->
//            parcelRepository.getParcelStatus(p.code)
//                    .withNetwork(networkInteractor)
//                    .subscribeOn(schedulerProvider.io())
//                    .observeOn(schedulerProvider.ui())
//                    .subscribeBy(onError = {
//                        Timber.e(it, "Could not update $i : ${it.message}")
//                    },
//                            onNext = {
//                                statusReports.value = it
//                            })
//                    .addTo(disposables)
//        }
//        localParcelRepository.getParcels()
//                .flatMapIterable {
//                    it
//                }
//                .map {
////                    Timber.d("Parcel poll checking parcel with code ${it.code}")
////                    correosRepository.getParcelStatus(it.code)
//                }
//                .toList()
//                .map { ListenableWorker.Result.success() }
//                .onErrorReturn {
//                    ListenableWorker.Result.failure()
//                }
    }


}