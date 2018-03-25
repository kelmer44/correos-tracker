package net.kelmer.correostracker.ui.list

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import net.kelmer.correostracker.data.model.CorreosApiParcel
import java.util.*

/**
 * Created by gabriel on 25/03/2018.
 */
class ParcelListViewModel : ViewModel() {


    val parcelList : MutableLiveData<List<CorreosApiParcel>> = MutableLiveData()


    fun retrieveParcelList() {


        Observable.just()



    }


}