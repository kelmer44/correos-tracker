package net.kelmer.correostracker.ui.create

import android.widget.Toast
import net.kelmer.correostracker.R
import net.kelmer.correostracker.base.BaseFragment
import net.kelmer.correostracker.data.Result
import net.kelmer.correostracker.ext.observe

/**
 * Created by gabriel on 25/03/2018.
 */
class CreateParcelFragment : BaseFragment<CreateParcelViewModel>() {
    override val viewModelClass = CreateParcelViewModel::class.java

    override fun loadUp() {
        viewModel.retrieveParcelDetails("")


        viewModel.parcelLiveData.observe(this, {
            when(it){
                is Result.Success -> {

                }

                is Result.Failure ->{
                    Toast.makeText(context, "ERROR!", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    override val layoutId: Int = R.layout.fragment_create_parcel
}