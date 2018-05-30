package net.kelmer.correostracker.ui.create

import android.text.TextUtils
import kotlinx.android.synthetic.main.fragment_create_parcel.*
import net.kelmer.correostracker.ApplicationComponent
import net.kelmer.correostracker.R
import net.kelmer.correostracker.base.BaseFragment
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.ext.observe

/**
 * Created by gabriel on 25/03/2018.
 */
class CreateParcelFragment : BaseFragment<CreateParcelViewModel>() {

    override fun injectDependencies(graph: ApplicationComponent) {
        val component = graph.plus(CreateParcelModule())
        component
                .injectTo(this)
        component
                .injectTo(viewModel)
    }

    override val viewModelClass = CreateParcelViewModel::class.java

    override fun loadUp() {
        create_ok.setOnClickListener {



            if(!TextUtils.isEmpty(parcel_name.text.toString()) && !TextUtils.isEmpty(parcel_code.text.toString())) {

                var checkedRadioButtonId = stance_group.getCheckedRadioButtonId()
                var stance = when (checkedRadioButtonId){
                    R.id.stance_incoming -> LocalParcelReference.Stance.INCOMING
                    else -> LocalParcelReference.Stance.OUTGOING
                }

                var localParcelReference = LocalParcelReference(parcel_code.text.toString(), parcel_name.text.toString(), stance, null)
                viewModel.addParcel(localParcelReference)

            }
        }
        viewModel.saveParcelLiveData.observe(this, {
           activity?.finish()
        })

    }


    override val layoutId: Int = R.layout.fragment_create_parcel
}