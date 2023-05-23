package net.kelmer.correostracker.ui.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Flowable
import io.reactivex.rxkotlin.combineLatest
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
import net.kelmer.correostracker.iap.IapApi
import net.kelmer.correostracker.list.ParcelListPreferences
import net.kelmer.correostracker.ui.theme.ThemeMode
import net.kelmer.correostracker.viewmodel.AutoDisposeViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    parcelListPreferences: ParcelListPreferences<ThemeMode>,
    iap: IapApi
) : AutoDisposeViewModel() {

    val stateOnceAndStream: Flowable<State> =
        parcelListPreferences.themeModeStream
            .combineLatest(
                iap.isPremium()
                    .map { PremiumState(it, true) }
                    .doOnError {
                        Timber.e(it, "Error on premium observable")
                    }
                    .onErrorReturnItem(
                        PremiumState(
                            isPremium = false,
                            isBillingAvailable = false
                        )
                    )
            )
            .map { (themeMode, premium) ->
                State(
                    theme = themeMode,
                    premiumState = premium
                )
            }
            .distinctUntilChanged()
            .replay(1)
            .connectInViewModelScope()

    fun sanitizeCode(code: String) = code.trim().replace("/", "")

    data class PremiumState(
        val isPremium: Boolean,
        val isBillingAvailable: Boolean
    )

    data class State(
        val isLoading: Boolean = false,
        val useDynamicColors: Boolean = false,
        val theme: ThemeMode = ThemeMode.SYSTEM,
        val premiumState: PremiumState = PremiumState(isPremium = false, isBillingAvailable = true)
    )
}
