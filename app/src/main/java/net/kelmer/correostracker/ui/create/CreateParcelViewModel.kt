package net.kelmer.correostracker.ui.create

import androidx.lifecycle.MutableLiveData
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import net.kelmer.correostracker.base.BaseViewModel
import net.kelmer.correostracker.data.Result
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.data.usecases.CreateParcel
import javax.inject.Inject

/**
 * Created by gabriel on 25/03/2018.
 */
class CreateParcelViewModel @Inject constructor(
    private val createParcel: CreateParcel
) : BaseViewModel() {

    var saveParcelLiveData: MutableLiveData<Result<Boolean>> = MutableLiveData()

    fun addParcel(localParcelReference: LocalParcelReference) {
        createParcel.execute(localParcelReference)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribeBy(onError = {
                saveParcelLiveData.value = Result.failure(it)
            },
                onSuccess = {
                    saveParcelLiveData.value = Result.success(true)
                })
            .addTo(disposables)
    }
}