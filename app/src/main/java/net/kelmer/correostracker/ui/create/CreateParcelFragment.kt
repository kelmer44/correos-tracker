package net.kelmer.correostracker.ui.create

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.zxing.integration.android.IntentIntegrator
import dagger.hilt.android.AndroidEntryPoint
import net.kelmer.correostracker.R
import net.kelmer.correostracker.base.fragment.BaseFragment
import net.kelmer.correostracker.customviews.ConfirmDialog
import net.kelmer.correostracker.data.Resource
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.data.network.exception.WrongCodeException
import net.kelmer.correostracker.data.resolve
import timber.log.Timber
import java.util.UUID
import android.view.View
import androidx.appcompat.widget.Toolbar
import net.kelmer.correostracker.databinding.FragmentCreateParcelBinding

/**
 * Created by gabriel on 25/03/2018.
 */
@AndroidEntryPoint
class CreateParcelFragment : BaseFragment<FragmentCreateParcelBinding>(R.layout.fragment_create_parcel) {


    private val viewModel: CreateParcelViewModel by viewModels()

    private val observeResult: (Resource<LocalParcelReference>) -> Unit = { resource ->
        resource.resolve(
                onSuccess = {
                    Timber.i("Parcel ${it.trackingCode} created!")
                    hideKeyboardFrom(requireContext(), requireView())
                    findNavController().navigate(R.id.nav_graph)
                },
                onError = {
                    Timber.e(it)
                    (it as? WrongCodeException)?.let {
                        ConfirmDialog.confirmDialog(requireContext(),
                                R.string.create_parcel_error_codigo_title,
                                R.string.create_parcel_error_codigo) {
                        }
                        Toast.makeText(context,
                                getString(R.string.create_parcel_error_codigo),
                                Toast.LENGTH_LONG)
                                .show()
                    }
                })
    }

    private fun hideKeyboardFrom(context: Context, view: View) {
        val imm: InputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun setupToolbar(toolbar: Toolbar) {
    }

    override fun loadUp(binding: FragmentCreateParcelBinding, savedInstanceState: Bundle?) {
        NavigationUI.setupWithNavController(binding.createToolbar, findNavController())
        setupToolbar(binding.createToolbar)

        binding.createOk.setOnClickListener {

            if (!TextUtils.isEmpty(binding.parcelCode.text.toString())) {
                //If no name is given we impose the code as the name
                if (TextUtils.isEmpty(binding.parcelName.text.toString())) {
                    binding.parcelName.text = binding.parcelCode.text
                }
                val stance = when (binding.stanceGroup.checkedRadioButtonId) {
                    R.id.stance_incoming -> LocalParcelReference.Stance.INCOMING
                    else -> LocalParcelReference.Stance.OUTGOING
                }
                val notify = binding.parcelStatusAlerts.isChecked
                val localParcelReference = LocalParcelReference(UUID.randomUUID().toString(), binding.parcelCode.text.toString(), binding.parcelCode.text.toString(), stance, null, notify = notify, updateStatus = LocalParcelReference.UpdateStatus.UNKNOWN)
                viewModel.addParcel(localParcelReference).observe(viewLifecycleOwner, observeResult)

            } else {
                binding.parcelCodeLayout.error = getString(R.string.error_nocodigo)
            }
        }

        binding.parcelCodeLayout
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
            binding?.parcelCode?.setText(it)
        }
    }

    override fun bind(view: View): FragmentCreateParcelBinding = FragmentCreateParcelBinding.bind(view)

}