package net.kelmer.correostracker.create

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import net.kelmer.correostracker.create.compose.CreateScreen
import net.kelmer.correostracker.create.databinding.FragmentCreateParcelBinding
import net.kelmer.correostracker.dataApi.model.local.LocalParcelReference
import net.kelmer.correostracker.ui.theme.CorreosTheme
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

class CreatePresenter @Inject constructor(
    private val fragment: Fragment,
) {
    private val binding = FragmentCreateParcelBinding.bind(fragment.requireView())
    private val viewModel: CreateParcelViewModel by fragment.viewModels()

    private val registerForActivityResult = fragment.registerForActivityResult(
        ScanContract()
    ) { result ->
        if (result.contents != null)
            binding.parcelCode.setText(result.contents)
    }

    init {

        NavigationUI.setupWithNavController(binding.createToolbar, findNavController(fragment))

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
                addParcel(
                    binding.parcelName.text.toString(),
                    binding.parcelCode.text.toString(),
                    stance,
                    notify
                )
            } else {
                binding.parcelCodeLayout.error = fragment.getString(R.string.error_nocodigo)
            }
        }

        binding.parcelCodeLayout.setEndIconOnClickListener {
            launchBarcodeScanner()
        }
    }

    private fun launchBarcodeScanner() {
        registerForActivityResult.launch(
            ScanOptions()
                .setOrientationLocked(false)
                .setBeepEnabled(true)
                .setBarcodeImageEnabled(true)
        )
    }

    private fun addParcel(
        parcelName: String,
        parcelCode: String,
        stance: LocalParcelReference.Stance,
        notify: Boolean,
    ) {
        viewModel.addParcel(
            LocalParcelReference(
                code = UUID.randomUUID().toString(),
                trackingCode = parcelCode,
                parcelName = parcelName,
                stance = stance,
                ultimoEstado = null,
                notify = notify,
                updateStatus = LocalParcelReference.UpdateStatus.UNKNOWN
            )
        )
    }

    fun bindState(state: CreateParcelViewModel.State) {

        binding.composeView.setContent {
            CorreosTheme {
                CreateScreen(
                    useDarkTheme = isSystemInDarkTheme(),
                    backAction = {
                        findNavController(fragment)
                            .popBackStack()
                    },
                    onScanClicked = {
                        launchBarcodeScanner()
                    }
                )
            }
        }

        if (state.savedParcel != null) {
            Timber.i("Parcel ${state.savedParcel.trackingCode} created!")
            hideKeyboardFrom(fragment.requireContext(), fragment.requireView())
            findNavController(fragment)
                .popBackStack()
        }

        if (state.error != null) {
            Timber.e(state.error)
        }
    }

    private fun hideKeyboardFrom(context: Context, view: View) {
        val imm: InputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


}
