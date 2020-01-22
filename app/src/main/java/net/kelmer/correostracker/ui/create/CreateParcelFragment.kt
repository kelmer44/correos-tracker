package net.kelmer.correostracker.ui.create

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.fragment_create_parcel.*
import net.kelmer.correostracker.R
import net.kelmer.correostracker.base.fragment.BaseFragment
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.ext.observe

/**
 * Created by gabriel on 25/03/2018.
 */
class CreateParcelFragment : BaseFragment<CreateParcelViewModel>() {

    override val viewModelClass = CreateParcelViewModel::class.java

    override fun loadUp(savedInstanceState: Bundle?) {
        create_ok.setOnClickListener {

            if (!TextUtils.isEmpty(parcel_code.text.toString())) {
                if (TextUtils.isEmpty(parcel_name.text.toString())) {
                    parcel_name.text = parcel_code.text
                }
                val stance = when (stance_group.checkedRadioButtonId) {
                    R.id.stance_incoming -> LocalParcelReference.Stance.INCOMING
                    else -> LocalParcelReference.Stance.OUTGOING
                }
                val notify = parcel_status_alerts.isChecked
                val localParcelReference = LocalParcelReference(parcel_code.text.toString(), parcel_name.text.toString(), stance, null, notify = notify)
                viewModel.addParcel(localParcelReference)

            } else {
                parcel_code_layout.error = getString(R.string.error_nocodigo)
            }
        }
        viewModel.saveParcelLiveData.observe(this) {
            activity?.setResult(Activity.RESULT_OK)
            activity?.finish()
        }

        parcel_code_layout
                .setEndIconOnClickListener {
                    tryToScanCode()
                }

    }

    private fun tryToScanCode() {
        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.setPrompt(getString(R.string.scan_prompt))
        integrator.setBeepEnabled(false)
        integrator.setBarcodeImageEnabled(true)
        integrator.initiateScan()
    }

    override fun onActivityResult(
            requestCode: Int,
            resultCode: Int,
            data: Intent?
    ) {
        val result =
                IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        result.contents?.let {
            parcel_code.setText(it)
        }
    }

    override val layoutId: Int = R.layout.fragment_create_parcel
}