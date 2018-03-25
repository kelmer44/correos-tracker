package net.kelmer.correostracker.ui.list

import android.arch.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import net.kelmer.correostracker.base.RxViewModel
import net.kelmer.correostracker.data.Result
import net.kelmer.correostracker.data.model.remote.CorreosApiParcel

/**
 * Created by gabriel on 25/03/2018.
 */
class ParcelListViewModel : RxViewModel() {

    val parcelList: MutableLiveData<Result<List<CorreosApiParcel>>> = MutableLiveData()


    fun retrieveParcelList() {


        var correosApiParcel = CorreosApiParcel("1234567890D", listOf())
        var correosApiParcel1 = CorreosApiParcel("0987654321Q", listOf())

        Observable.just(listOf(correosApiParcel, correosApiParcel1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onError = {
                    parcelList.value = Result.failure("ERROR", it)
                },
                        onNext = { p ->
                            parcelList.value = Result.success(p)
                        })
                .addTo(disposables)
    }


}