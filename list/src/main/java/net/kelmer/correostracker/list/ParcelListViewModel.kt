package net.kelmer.correostracker.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.uber.autodispose.autoDisposable
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.processors.PublishProcessor
import io.reactivex.rxkotlin.Flowables
import io.reactivex.rxkotlin.combineLatest
import net.kelmer.correostracker.BuildInfo
import net.kelmer.correostracker.dataApi.Resource
import net.kelmer.correostracker.dataApi.model.local.LocalParcelReference
import net.kelmer.correostracker.dataApi.repository.local.LocalParcelRepository
import net.kelmer.correostracker.iap.IapApi
import net.kelmer.correostracker.iap.ProductDetails
import net.kelmer.correostracker.list.feature.Feature
import net.kelmer.correostracker.list.usecases.notifications.SwitchNotificationsUseCase
import net.kelmer.correostracker.list.usecases.statusreports.StatusReportsUpdatesUseCase
import net.kelmer.correostracker.ui.theme.ThemeMode
import net.kelmer.correostracker.util.SchedulerProvider
import net.kelmer.correostracker.viewmodel.AutoDisposeViewModel
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by gabriel on 25/03/2018.
 */
@HiltViewModel
class ParcelListViewModel @Inject constructor(
    private val localParcelRepository: LocalParcelRepository,
    private val switchNotificationsUseCase: SwitchNotificationsUseCase,
    private val statusReportsUpdatesUseCase: StatusReportsUpdatesUseCase,
    private val parcelListPreferences: ParcelListPreferences<ThemeMode>,
    private val buildInfo: BuildInfo,
    private val schedulerProvider: SchedulerProvider,
    iapApi: IapApi
) : AutoDisposeViewModel() {

    private val filterSubject: PublishProcessor<String> = PublishProcessor.create()

    val stateOnceAndStream =
        Flowables.combineLatest(
            localParcelRepository.getParcels(),
            filterSubject.startWith(""),
            parcelListPreferences.themeModeStream,
            iapApi.getProductDetails().toFlowable().startWith(ProductDetails("", "", ""))
        )
        { list, filter, theme, product ->
            State(
                list = list.filter {
                    filter.isNullOrBlank() ||
                        it.parcelName.contains(filter, true) ||
                        it.trackingCode.contains(filter, true)
                },
                filter = filter,
                theme = theme,
                price = product.price
            )
        }
        .startWith(State(loading = true))
        .onErrorReturn { throwable -> State(error = throwable) }
        .distinctUntilChanged()
        .replay(1)
        .connectInViewModelScope()

    init {
        refresh()
    }

    fun getFeatureList(): List<Feature> {
        return listOf(
            Feature("3.0.0", R.string.changes_3_0_0),
            Feature("2.3.3", R.string.changes_2_3_3),
            Feature("2.3.2", R.string.changes_2_3_2),
            Feature("2.2.7", R.string.changes_2_2_7),
            Feature("2.2.6", R.string.changes_2_2_6),
            Feature("2.1.0", R.string.changes_2_1_0),
            Feature("2.0.0", R.string.changes_2_0_0),
            Feature("1.9.5", R.string.changes_1_9_5),
            Feature("1.9.0", R.string.changes_1_9_0),
            Feature("1.8.0", R.string.changes_1_8_0),
        )
    }

    fun deleteParcel(parcelReference: LocalParcelReference) {
        localParcelRepository.deleteParcel(parcelReference)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .autoDisposable(viewModelScope)
            .subscribe(
                { Timber.w("Deleted parcel = ${parcelReference.trackingCode}!!") },
                {
                    Timber.e(it)
                }
            )
    }

    fun refresh() {
        Timber.i("REF - Refresh called!")
        statusReportsUpdatesUseCase(Unit, MutableLiveData())
    }

    fun toggleNotifications(code: String, enabled: Boolean) {
        if (enabled) {
            enableNotifications(code)
        } else {
            disableNotifications(code)
        }
    }

    fun enableNotifications(code: String): LiveData<Resource<String>> {
        return switchNotificationsUseCase(SwitchNotificationsUseCase.Params(code, true))
    }

    fun disableNotifications(code: String): LiveData<Resource<String>> {
        return switchNotificationsUseCase(SwitchNotificationsUseCase.Params(code, false))
    }

    fun showFeature(): Boolean {
        return parcelListPreferences.hasSeenFeatureBlurb(buildInfo.versionName)
    }

    fun setShownFeature() {
        parcelListPreferences.setSeenFeatureBlurb(buildInfo.versionName)
    }

    fun setTheme(theme: ThemeMode) {
        parcelListPreferences.theme = theme
    }

    fun filter(newText: String) = filterSubject.onNext(newText)

    data class State(
        val list: List<LocalParcelReference>? = null,
        val loading: Boolean = false,
        val error: Throwable? = null,
        val filter: String? = null,
        val theme: ThemeMode? = null,
        val price: String? = null
    )
}
