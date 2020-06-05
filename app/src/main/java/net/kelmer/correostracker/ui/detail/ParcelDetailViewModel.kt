package net.kelmer.correostracker.ui.detail

import net.kelmer.correostracker.base.BaseViewModel
import net.kelmer.correostracker.usecases.details.GetParcelUseCase
import javax.inject.Inject


class ParcelDetailViewModel @Inject constructor(private val getParcelUseCase: GetParcelUseCase) : BaseViewModel(getParcelUseCase) {

    fun getParcel(parcelCode: String) = getParcelUseCase(GetParcelUseCase.Params(parcelCode))
}