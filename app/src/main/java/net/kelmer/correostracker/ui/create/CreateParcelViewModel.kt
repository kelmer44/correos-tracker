package net.kelmer.correostracker.ui.create

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import net.kelmer.correostracker.base.BaseViewModel
import net.kelmer.correostracker.data.Resource
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.data.model.remote.CorreosApiParcel
import net.kelmer.correostracker.usecases.create.CreateParcelUseCase
import javax.inject.Inject

/**
 * Created by gabriel on 25/03/2018.
 */
class CreateParcelViewModel @ViewModelInject constructor(
        private val createParcelUseCase: CreateParcelUseCase
) : BaseViewModel() {

    fun addParcel(localParcelReference: LocalParcelReference): LiveData<Resource<LocalParcelReference>> {
       return createParcelUseCase(CreateParcelUseCase.Params(localParcelReference))
    }
}