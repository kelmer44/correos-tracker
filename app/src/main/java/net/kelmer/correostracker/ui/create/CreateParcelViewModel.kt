package net.kelmer.correostracker.ui.create

import android.arch.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import net.kelmer.correostracker.base.RxViewModel
import net.kelmer.correostracker.data.Result
import net.kelmer.correostracker.data.model.CorreosApiParcel

/**
 * Created by gabriel on 25/03/2018.
 */
class CreateParcelViewModel : RxViewModel() {


    val parcelLiveData = MutableLiveData<Result<CorreosApiParcel>>()

    fun retrieveParcelDetails(code: String) {


        var correosApiParcel = CorreosApiParcel("DSFDSFASD1", listOf())

        Observable.just(correosApiParcel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy  (
                        onError = {
                           parcelLiveData.value =  Result.failure("ERROR", it)
                        },
                        onNext = {
                            parcelLiveData.value = Result.success(it)
                        }
                )
                .addTo(disposables)

    }


}