package net.kelmer.correostracker.ui.create

import android.arch.lifecycle.MutableLiveData
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import net.kelmer.correostracker.base.RxViewModel
import net.kelmer.correostracker.data.Result
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.data.repository.local.LocalParcelRepository
import javax.inject.Inject

/**
 * Created by gabriel on 25/03/2018.
 */
class CreateParcelViewModel : RxViewModel() {

    @Inject
    lateinit var localParcelRepository: LocalParcelRepository


    var saveParcelLiveData: MutableLiveData<Result<Boolean>> = MutableLiveData()

    fun addParcel(localParcelReference: LocalParcelReference) {
        localParcelRepository.saveParcel(localParcelReference)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeBy(onError = {
                    saveParcelLiveData.value = Result.failure(it)
                },
                        onComplete = {
                            saveParcelLiveData.value = Result.success(true)
                        })
                .addTo(disposables)
    }

}