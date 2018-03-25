package net.kelmer.correostracker.ui.detail

import android.arch.lifecycle.MutableLiveData
import net.kelmer.correostracker.base.RxViewModel
import net.kelmer.correostracker.data.Result
import net.kelmer.correostracker.data.model.local.LocalParcelReference


class ParcelDetailViewModel : RxViewModel() {

    val parcel: MutableLiveData<Result<LocalParcelReference>> = MutableLiveData()
}