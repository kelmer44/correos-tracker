package net.kelmer.correostracker.ui.list

import android.arch.lifecycle.MutableLiveData
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import net.kelmer.correostracker.base.RxViewModel
import net.kelmer.correostracker.data.Result
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.data.repository.local.LocalParcelRepository
import net.kelmer.correostracker.ext.toResult
import javax.inject.Inject

/**
 * Created by gabriel on 25/03/2018.
 */
class ParcelListViewModel : RxViewModel() {

    val parcelList: MutableLiveData<Result<List<LocalParcelReference>>> = MutableLiveData()

    @Inject
    lateinit var localParcelRepository: LocalParcelRepository


    fun retrieveParcelList() {
        localParcelRepository.getParcels()
                .toResult(schedulerProvider)
                .subscribeBy (
                        onNext = {
                            parcelList.value =  it
                        }
                ).addTo(disposables)
    }


}