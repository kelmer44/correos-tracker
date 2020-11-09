package net.kelmer.correostracker.ui.detail

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import net.kelmer.correostracker.base.BaseViewModel
import net.kelmer.correostracker.data.Resource
import net.kelmer.correostracker.data.model.dto.ParcelDetailDTO
import net.kelmer.correostracker.usecases.details.GetParcelUseCase
import javax.inject.Inject


class ParcelDetailViewModel @ViewModelInject constructor(
        private val getParcelUseCase: GetParcelUseCase,
        @Assisted private val savedStateHandle: SavedStateHandle
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