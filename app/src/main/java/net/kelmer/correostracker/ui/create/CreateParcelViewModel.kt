package net.kelmer.correostracker.ui.create

import androidx.lifecycle.MutableLiveData
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import net.kelmer.correostracker.base.BaseViewModel
import net.kelmer.correostracker.data.Resource
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.data.repository.local.LocalParcelRepository
import net.kelmer.correostracker.data.usecases.CreateParcel
import javax.inject.Inject

/**
 * Created by gabriel on 25/03/2018.
 */
class CreateParcelViewModel @Inject constructor(
        private val createParcel: CreateParcel,
        private val localParcelRepository: LocalParcelRepository
) : BaseViewModel() {

    var saveParcelLiveData: MutableLiveData<Resource<Boolean>> = MutableLiveData()

    fun addParcel(localParcelReference: LocalParcelReference) {
        localParcelRepository.saveParcel(localParcelReference)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeBy(onError = {
                    saveParcelLiveData.value = Resource.failure(it)
                },
                        onComplete = {
                            saveParcelLiveData.value = Resource.success(true)
                        })
                .addTo(disposables)
    }

    fun addParcelWithCheck(localParcelReference: LocalParcelReference) {
        createParcel.execute(localParcelReference)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeBy(onError = {
                    saveParcelLiveData.value = Resource.failure(it)
                },
                        onSuccess = {
                            saveParcelLiveData.value = Resource.success(true)
                        })
                .addTo(disposables)
    }
}