package net.kelmer.correostracker.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.uber.autodispose.autoDisposable
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Flowable
import io.reactivex.processors.PublishProcessor
import io.reactivex.rxkotlin.combineLatest
import net.kelmer.correostracker.BuildInfo
import net.kelmer.correostracker.dataApi.Resource
import net.kelmer.correostracker.dataApi.model.local.LocalParcelReference
import net.kelmer.correostracker.dataApi.repository.local.LocalParcelRepository
//import net.kelmer.correostracker.data.prefs.SharedPrefsManager
import net.kelmer.correostracker.list.notifications.SwitchNotificationsUseCase
import net.kelmer.correostracker.list.preferences.ParcelListPreferencesImpl
import net.kelmer.correostracker.list.statusreports.StatusReportsUpdatesUseCase
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
    private val parcelListPreferences: ParcelListPreferencesImpl,
    private val buildInfo: BuildInfo,
    private val schedulerProvider: SchedulerProvider
) : AutoDisposeViewModel() {

    private val filterSubject: PublishProcessor<String> = PublishProcessor.create()

    val stateOnceAndStream =
        localParcelRepository.getParcels()
            .combineLatest(
                filterSubject.startWith("")
            )
            .map { (list, filter) ->
                State(
                    list = list.filter {
                    filter.isNullOrBlank() ||
                        it.parcelName.contains(filter, true) ||
                        it.trackingCode.contains(filter, true)
                }
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

    fun setTheme(theme: Int) {
        parcelListPreferences.themeMode = theme
    }

    fun filter(newText: String) = filterSubject.onNext(newText)

    data class State(
        val list: List<LocalParcelReference>? = null,
        val loading: Boolean = false,
        val error: Throwable? = null,
    )
}
