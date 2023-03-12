package net.kelmer.correostracker.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import net.kelmer.correostracker.data.Resource
import net.kelmer.correostracker.data.model.dto.ParcelDetailDTO
import net.kelmer.correostracker.detail.usecase.GetParcelUseCase
import net.kelmer.correostracker.viewmodel.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class ParcelDetailViewModel @Inject constructor(
    private val getParcelUseCase: GetParcelUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel(getParcelUseCase) {

    private val parcelCode: String? = savedStateHandle.get<String>(DetailFragment.KEY_PARCELCODE)
    private val _statusResult: MutableLiveData<Resource<ParcelDetailDTO>> = MutableLiveData()
    val statusResult: LiveData<Resource<ParcelDetailDTO>> = _statusResult

    init {
        if (parcelCode != null) {
            getParcel(parcelCode)
        }
    }

    private fun getParcel(parcelCode: String) = getParcelUseCase(GetParcelUseCase.Params(parcelCode), _statusResult)
    fun refresh() {
        parcelCode?.let {
            getParcel(parcelCode)
        }
    }
}
