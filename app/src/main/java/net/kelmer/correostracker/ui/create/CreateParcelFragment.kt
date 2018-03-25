package net.kelmer.correostracker.ui.create

import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_create_parcel.*
import net.kelmer.correostracker.R
import net.kelmer.correostracker.base.BaseFragment
import net.kelmer.correostracker.data.Result
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.ext.observe

/**
 * Created by gabriel on 25/03/2018.
 */
class CreateParcelFragment : BaseFragment<CreateParcelViewModel>() {
    override val viewModelClass = CreateParcelViewModel::class.java

    override fun loadUp() {

        create_ok.setOnClickListener {


            var localParcelReference = LocalParcelReference(parcel_name.text.toString(), parcel_code.text.toString())
            viewModel.addParcel(localParcelReference)
        }

        viewModel.retrieveParcelDetails("")
        viewModel.parcelLiveData.observe(this, {
            when (it) {
                is Result.Success -> {

                }

                is Result.Failure -> {
                    Toast.makeText(context, "ERROR!", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    override val layoutId: Int = R.layout.fragment_create_parcel
}