package net.kelmer.correostracker.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
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
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by gabriel on 25/03/2018.
 */
class ParcelListViewModel @Inject constructor(
        private val getParcelListUseCase: GetParcelListUseCase,
        private val deleteParcelUseCase: DeleteParcelUseCase,
        private val switchNotificationsUseCase: SwitchNotificationsUseCase,
        private val parcelRepository: CorreosRepository,
        private val sharedPrefsManager: SharedPrefsManager)
    : BaseViewModel(
        getParcelListUseCase,
        deleteParcelUseCase,
        switchNotificationsUseCase) {


    private val _parcelList: MutableLiveData<Resource<List<LocalParcelReference>>> = MutableLiveData()
    fun getParcelList() = _parcelList
    fun retrieveParcelList() = getParcelListUseCase(Unit, _parcelList)

    private val _deleteLiveData: MutableLiveData<Resource<Unit>> = MutableLiveData()
    fun getDeleteResult() = _deleteLiveData
    fun deleteParcel(parcelReference: LocalParcelReference) {
        deleteParcelUseCase(DeleteParcelUseCase.Params(parcelReference), _deleteLiveData)
    }

    val statusReports: MutableLiveData<CorreosApiParcel> = MutableLiveData()
    fun refresh(items: List<LocalParcelReference>) {
        items.forEachIndexed { i, p ->
            parcelRepository.getParcelStatus(p.trackingCode)
                    .withNetwork(networkInteractor)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribeBy(
                            onError = {
                                if (it is WrongCodeException) {
                                    Timber.w("Wrong code: ${p.trackingCode}!")
                                } else {
                                    Timber.e(it, "Could not update $i : ${it.message}")
                                }
                            },
                            onSuccess = {
                                statusReports.value = it
                            }
                    )
                    .addTo(disposables)
        }
    }

    fun enableNotifications(code: String): LiveData<Resource<String>> {
        return switchNotificationsUseCase(SwitchNotificationsUseCase.Params(code, true))
    }

    fun disableNotifications(code: String): LiveData<Resource<String>> {
        return switchNotificationsUseCase(SwitchNotificationsUseCase.Params(code, false))
    }

    fun showFeature(): Boolean {
        return sharedPrefsManager.hasSeenFeatureBlurb()
    }

    fun setShownFeature() {
        sharedPrefsManager.setSeenFeatureBlurb()
    }


    fun setTheme(theme: Int) {
        sharedPrefsManager.themeMode = theme
    }

}