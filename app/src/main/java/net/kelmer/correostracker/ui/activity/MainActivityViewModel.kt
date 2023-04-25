package net.kelmer.correostracker.ui.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Flowable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.kelmer.correostracker.list.ParcelListPreferences
import net.kelmer.correostracker.ui.theme.ThemeMode
import net.kelmer.correostracker.viewmodel.AutoDisposeViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val parcelListPreferences: ParcelListPreferences<ThemeMode>
) : AutoDisposeViewModel() {


//    val state =
//        parcelListPreferences.themeModeStream.map {
//            State(
//                isLoading = false,
//                useDynamicColors = false,
//                theme = it
//            )
//        }.stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
//            initialValue = State(theme = parcelListPreferences.theme)
//        )


//    val stateOnceAndStream =

    //
    val stateOnceAndStream : Flowable<State> =
        parcelListPreferences.themeModeStream
            .doOnNext {
                Timber.w("THEME - got new value! $it")
            }
            .map { State(theme = it) }
            .distinctUntilChanged()
            .replay(1)
            .connectInViewModelScope()

    data class State(
        val isLoading: Boolean = false,
        val useDynamicColors: Boolean = false,
        val theme: ThemeMode = ThemeMode.SYSTEM
    )
}
