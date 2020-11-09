package net.kelmer.correostracker.ui.list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.work.ListenableWorker
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import net.kelmer.correostracker.BuildConfig
import net.kelmer.correostracker.base.BaseViewModel
import net.kelmer.correostracker.data.Resource
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.data.model.remote.CorreosApiParcel
import net.kelmer.correostracker.data.network.exception.WrongCodeException
import net.kelmer.correostracker.data.prefs.SharedPrefsManager
import net.kelmer.correostracker.data.repository.correos.CorreosRepository
import net.kelmer.correostracker.ext.withNetwork
import net.kelmer.correostracker.usecases.delete.DeleteParcelUseCase
import net.kelmer.correostracker.usecases.list.GetParcelListUseCase
import net.kelmer.correostracker.usecases.notifications.SwitchNotificationsUseCase
import net.kelmer.correostracker.usecases.statusreports.StatusReportsUpdatesUseCase
import net.kelmer.correostracker.util.NetworkInteractor
import net.kelmer.correostracker.util.SchedulerProvider
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by gabriel on 25/03/2018.
 */
class ParcelListViewModel @ViewModelInject constructor(
        private val getParcelListUseCase: GetParcelListUseCase,
        private val deleteParcelUseCase: DeleteParcelUseCase,
        private val switchNotificationsUseCase: SwitchNotificationsUseCase,
        private val statusReportsUpdatesUseCase: StatusReportsUpdatesUseCase,
        private val sharedPrefsManager: SharedPrefsManager)
    : BaseViewModel(
        getParcelListUseCase,
        deleteParcelUseCase,
        switchNotificationsUseCase) {

    private val _parcelList: MutableLiveData<Resource<List<LocalParcelReference>>> = MutableLiveData()
    fun getParcelList(): LiveData<Resource<List<LocalParcelReference>>> = _parcelList
    fun retrieveParcelList() = getParcelListUseCase(Unit, _parcelList)

    init {
        retrieveParcelList()
        val mediatorLiveData = MediatorLiveData<List<LocalParcelReference>>()
        mediatorLiveData.addSource(_parcelList) {
            Timber.i("REF - Emission from parcel list: $it")
            if (it is Resource.Success) {
                refresh(it.data)
            }
        }
    }


    private val _deleteLiveData: MutableLiveData<Resource<Unit>> = MutableLiveData()
    fun getDeleteResult(): LiveData<Resource<Unit>> = _deleteLiveData
    fun deleteParcel(parcelReference: LocalParcelReference) {
        deleteParcelUseCase(DeleteParcelUseCase.Params(parcelReference), _deleteLiveData)
    }

    val statusReports: MutableLiveData<Resource<CorreosApiParcel>> = MutableLiveData()
    fun refresh(items: List<LocalParcelReference>) {
        Timber.i("REF - Refresh called!")
        statusReportsUpdatesUseCase(StatusReportsUpdatesUseCase.Params(items), statusReports)
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