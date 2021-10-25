package net.kelmer.correostracker.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import net.kelmer.correostracker.BuildConfig
import net.kelmer.correostracker.base.BaseViewModel
import net.kelmer.correostracker.data.Resource
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.data.model.remote.CorreosApiParcel
import net.kelmer.correostracker.data.prefs.SharedPrefsManager
import net.kelmer.correostracker.usecases.delete.DeleteParcelUseCase
import net.kelmer.correostracker.usecases.list.GetParcelListUseCase
import net.kelmer.correostracker.usecases.notifications.SwitchNotificationsUseCase
import net.kelmer.correostracker.usecases.statusreports.StatusReportsUpdatesUseCase
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by gabriel on 25/03/2018.
 */
@HiltViewModel
class ParcelListViewModel @Inject constructor(
    private val getParcelListUseCase: GetParcelListUseCase,
    private val deleteParcelUseCase: DeleteParcelUseCase,
    private val switchNotificationsUseCase: SwitchNotificationsUseCase,
    private val statusReportsUpdatesUseCase: StatusReportsUpdatesUseCase,
    private val sharedPrefsManager: SharedPrefsManager
) :
    BaseViewModel(
        getParcelListUseCase,
        deleteParcelUseCase,
        switchNotificationsUseCase
    ) {

    private val _parcelList: MutableLiveData<Resource<List<LocalParcelReference>>> = MutableLiveData()
    val parcelList: LiveData<Resource<List<LocalParcelReference>>> = _parcelList
    private fun retrieveParcelList() = getParcelListUseCase(Unit, _parcelList)
    private val statusReports: MutableLiveData<Resource<CorreosApiParcel>> = MutableLiveData()

    init {
        retrieveParcelList()
        refresh()
    }

    private val _deleteLiveData: MutableLiveData<Resource<Unit>> = MutableLiveData()
    val deleteResult: LiveData<Resource<Unit>> = _deleteLiveData
    fun deleteParcel(parcelReference: LocalParcelReference) {
        deleteParcelUseCase(DeleteParcelUseCase.Params(parcelReference), _deleteLiveData)
    }

    fun refresh() {
        Timber.i("REF - Refresh called!")
        statusReportsUpdatesUseCase(Unit, statusReports)
    }

    fun enableNotifications(code: String): LiveData<Resource<String>> {
        return switchNotificationsUseCase(SwitchNotificationsUseCase.Params(code, true))
    }

    fun disableNotifications(code: String): LiveData<Resource<String>> {
        return switchNotificationsUseCase(SwitchNotificationsUseCase.Params(code, false))
    }

    fun showFeature(): Boolean {
        return sharedPrefsManager.hasSeenFeatureBlurb(BuildConfig.VERSION_NAME)
    }

    fun setShownFeature() {
        sharedPrefsManager.setSeenFeatureBlurb(BuildConfig.VERSION_NAME)
    }

    fun setTheme(theme: Int) {
        sharedPrefsManager.themeMode = theme
    }
}
