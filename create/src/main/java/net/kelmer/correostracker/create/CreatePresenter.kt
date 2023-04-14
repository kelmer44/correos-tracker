package net.kelmer.correostracker.create

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import net.kelmer.correostracker.create.compose.CreateScreen
import net.kelmer.correostracker.create.databinding.FragmentCreateParcelBinding
import net.kelmer.correostracker.dataApi.model.local.LocalParcelReference
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject


class CreatePresenter @Inject constructor(
    private val fragment: Fragment, private val viewModel: CreateParcelViewModel
) {
    private val binding = FragmentCreateParcelBinding.bind(fragment.requireView())

    init {

        NavigationUI.setupWithNavController(binding.createToolbar, findNavController(fragment))

        val registerForActivityResult = fragment.registerForActivityResult(
            ScanContract()
        ) { result ->
            if (result.contents != null)
                binding.parcelCode.setText(result.contents)
        }

        binding.createOk.setOnClickListener {
            if (!TextUtils.isEmpty(binding.parcelCode.text.toString())) {
                // If no name is given we impose the code as the name
                if (TextUtils.isEmpty(binding.parcelName.text.toString())) {
                    binding.parcelName.text = binding.parcelCode.text
                }
                val stance = when (binding.stanceGroup.checkedRadioButtonId) {
                    R.id.stance_incoming -> LocalParcelReference.Stance.INCOMING
                    else -> LocalParcelReference.Stance.OUTGOING
                }
                val notify = binding.parcelStatusAlerts.isChecked
                val localParcelReference = LocalParcelReference(
                    UUID.randomUUID().toString(),
                    binding.parcelCode.text.toString(),
                    binding.parcelName.text.toString(),
                    stance,
                    null,
                    notify = notify,
                    updateStatus = LocalParcelReference.UpdateStatus.UNKNOWN
                )
                viewModel.addParcel(localParcelReference)
            } else {
                binding.parcelCodeLayout.error = fragment.getString(R.string.error_nocodigo)
            }
        }

        binding.parcelCodeLayout.setEndIconOnClickListener {
            registerForActivityResult.launch(
                ScanOptions()
                    .setOrientationLocked(false)
                    .setBeepEnabled(true)
                    .setBarcodeImageEnabled(true)
            )
        }
    }

    fun bindState(state: CreateParcelViewModel.State) {

        binding.composeView.setContent {
            CreateScreen(
                state = state,
                backAction = {
                    findNavController(fragment)
                        .popBackStack()
                }) { form ->
                Timber.i("Form result = $form")
            }
        }

        if (state.savedParcel != null) {
            Timber.i("Parcel ${state.savedParcel.trackingCode} created!")
            hideKeyboardFrom(fragment.requireContext(), fragment.requireView())
            findNavController(fragment)
                .popBackStack()
        }
        if (state.isLoading) {

        }
        if (state.error != null) {
            Timber.e(state.error)
//            (it as? WrongCodeException)?.let {
//                    ConfirmDialog.confirmDialog(
//                        requireContext(),
//                        R.string.create_parcel_error_codigo_title,
//                        R.string.create_parcel_error_codigo
//                    ) {
//                    }
//                    Toast.makeText(
//                        context,
//                        getString(R.string.create_parcel_error_codigo),
//                        Toast.LENGTH_LONG
//                    )
//                        .show()
//            }
        }
    }

    private fun hideKeyboardFrom(context: Context, view: View) {
        val imm: InputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


}
