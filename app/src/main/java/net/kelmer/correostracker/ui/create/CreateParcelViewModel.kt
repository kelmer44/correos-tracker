package net.kelmer.correostracker.ui.create

import net.kelmer.correostracker.base.RxViewModel
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.data.repository.local.LocalParcelRepository
import javax.inject.Inject

/**
 * Created by gabriel on 25/03/2018.
 */
class CreateParcelViewModel : RxViewModel() {

    @Inject
    lateinit var localParcelRepository : LocalParcelRepository

    fun addParcel(localParcelReference: LocalParcelReference) {
        localParcelRepository.saveParcel(localParcelReference)
    }


}