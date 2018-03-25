package net.kelmer.correostracker.ui.list

import android.arch.lifecycle.MutableLiveData
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import net.kelmer.correostracker.base.RxViewModel
import net.kelmer.correostracker.data.Result
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.data.repository.local.LocalParcelRepository
import net.kelmer.correostracker.ext.toResult
import net.kelmer.correostracker.ext.withNetwork
import net.kelmer.correostracker.util.NetworkInteractor
import net.kelmer.correostracker.util.SchedulerProvider
import javax.inject.Inject

/**
 * Created by gabriel on 25/03/2018.
 */
class ParcelListViewModel : RxViewModel() {

    val parcelList: MutableLiveData<Result<List<LocalParcelReference>>> = MutableLiveData()

    @Inject
    lateinit var localParcelRepository: LocalParcelRepository


    fun retrieveParcelList() {


//        var correosApiParcel = CorreosApiParcel("1234567890D", listOf())
//        var correosApiParcel1 = CorreosApiParcel("0987654321Q", listOf())
//
//        Observable.just(listOf(correosApiParcel, correosApiParcel1))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeBy(onError = {
//                    parcelList.value = Result.failure("ERROR", it)
//                },
//                        onNext = { p ->
//                            parcelList.value = Result.success(p)
//                        })
//                .addTo(disposables)


        localParcelRepository.getParcels()
                .withNetwork(networkInteractor)
                .toResult(schedulerProvider)
                .subscribeBy (
                        onNext = {
                            parcelList.value =  it
                        }
                ).addTo(disposables)
    }


}