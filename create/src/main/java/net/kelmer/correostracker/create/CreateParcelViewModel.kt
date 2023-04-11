package net.kelmer.correostracker.create

import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.processors.PublishProcessor
import net.kelmer.correostracker.dataApi.model.local.LocalParcelReference
import net.kelmer.correostracker.dataApi.repository.local.LocalParcelRepository
import net.kelmer.correostracker.util.SchedulerProvider
import net.kelmer.correostracker.viewmodel.AutoDisposeViewModel
import javax.inject.Inject

/**
 * Created by gabriel on 25/03/2018.
 */

@HiltViewModel
class CreateParcelViewModel @Inject constructor(
    private val localParcelRepository: LocalParcelRepository,
    private val schedulerProvider: SchedulerProvider
) : AutoDisposeViewModel() {

    private val publishParcel = PublishProcessor.create<LocalParcelReference>()

    val stateOnceAndStream = publishParcel
        .flatMap {
            localParcelRepository.saveParcel(it)
                .andThen(localParcelRepository.getParcel(it.trackingCode))
                .subscribeOn(schedulerProvider.io())
        }
        .map { State(savedParcel = it) }
        .startWith(State())
        .onErrorReturn { State(error = it) }
        .subscribeOn(schedulerProvider.io())
        .distinctUntilChanged()
        .replay(1)
        .connectInViewModelScope()

    fun addParcel(localParcelReference: LocalParcelReference) = publishParcel.onNext(localParcelReference)

    data class State(
        val savedParcel: LocalParcelReference? = null,
        val isLoading: Boolean = false,
        val error: Throwable? = null
    )
}
