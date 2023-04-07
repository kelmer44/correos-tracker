package net.kelmer.correostracker.create

import androidx.lifecycle.LiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import net.kelmer.correostracker.create.create.CreateParcelUseCase
import net.kelmer.correostracker.viewmodel.BaseViewModel
import net.kelmer.correostracker.dataApi.Resource
import net.kelmer.correostracker.dataApi.model.local.LocalParcelReference
import javax.inject.Inject

/**
 * Created by gabriel on 25/03/2018.
 */

@HiltViewModel
class CreateParcelViewModel @Inject constructor(
    private val createParcelUseCase: CreateParcelUseCase
) : BaseViewModel() {

    fun addParcel(localParcelReference: LocalParcelReference): LiveData<Resource<LocalParcelReference>> {
        return createParcelUseCase(CreateParcelUseCase.Params(localParcelReference))
    }
}
