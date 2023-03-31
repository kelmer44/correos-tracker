package net.kelmer.correostracker.detail

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.processors.PublishProcessor
import net.kelmer.correostracker.data.model.dto.ParcelDetailDTO
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.data.model.remote.CorreosApiEvent
import net.kelmer.correostracker.data.model.remote.CorreosApiParcel
import net.kelmer.correostracker.data.repository.correos.CorreosRepository
import net.kelmer.correostracker.data.repository.local.LocalParcelRepository
import net.kelmer.correostracker.util.SchedulerProvider
import net.kelmer.correostracker.viewmodel.AutoDisposeViewModel
import timber.log.Timber
import javax.inject.Inject

class ParcelDetailViewModel @Inject constructor(
    private val parcelCode: String,
    localParcelRepository: LocalParcelRepository,
    private val correosRepository: CorreosRepository,
    private val schedulerProvider: SchedulerProvider
) : AutoDisposeViewModel() {

    init {
        Timber.i("Detail - ViewModel Created!")
    }

    private val refreshSubject = BehaviorProcessor.createDefault(Unit)

    val stateOnceAndStream =
        Flowable
            .combineLatest(
                localParcelRepository.getParcel(parcelCode).subscribeOn(schedulerProvider.io()),
                refreshSubject
                    .switchMapSingle {
                        correosRepository.getParcelStatus(parcelCode)
                            .subscribeOn(schedulerProvider.io())
                            .map { it.eventos ?: emptyList() }
                    }.startWith(emptyList<CorreosApiEvent>())
            ) { local, remote ->
                State(
                    local,
                    remote
                )
            }
            .subscribeOn(schedulerProvider.io())
            .startWith(State(isLoading = true))
            .onErrorReturn { throwable -> State(error = throwable) }
            .distinctUntilChanged()
            .replay(1)
            .connectInViewModelScope()

    fun refresh() = refreshSubject.onNext(Unit)

    data class State(
        val parcelDetail: LocalParcelReference? = null,
        val events: List<CorreosApiEvent>? = null,
        val isLoading: Boolean = false,
        val error: Throwable? = null
    )
}
