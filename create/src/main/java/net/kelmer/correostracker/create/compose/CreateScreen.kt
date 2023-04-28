package net.kelmer.correostracker.create.compose

import android.os.Parcelable
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava2.subscribeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import kotlinx.parcelize.Parcelize
import net.kelmer.correostracker.create.CreateParcelViewModel
import net.kelmer.correostracker.dataApi.model.local.LocalParcelReference
import androidx.lifecycle.viewmodel.compose.viewModel
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import net.kelmer.correostracker.create.R
import timber.log.Timber
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CreateScreen(
    useDarkTheme: Boolean,
    modifier: Modifier = Modifier,
    viewModel: CreateParcelViewModel = viewModel(),
    backAction: () -> Unit = {},
) {
    val viewState by viewModel.stateOnceAndStream.subscribeAsState(CreateParcelViewModel.State())
    Timber.w("Got app state $viewState")
    var formResult by rememberSaveable {
        mutableStateOf(
            Form("", "", true, LocalParcelReference.Stance.INCOMING)
        )
    }
    val noCodeError = stringResource(id = R.string.error_nocodigo)
    var validationError by rememberSaveable { mutableStateOf("") }
    val validationAction: () -> Unit = {
        validationError = noCodeError
    }

    val barcode = rememberLauncherForActivityResult(contract = ScanContract(), onResult = { result ->
        if (result.contents != null && result.contents.isNotBlank()) {
            validationError = ""
            formResult = formResult.copy(trackingCode = result.contents)
        }
    })
    if (viewState.savedParcel != null) {
        backAction()
    } else {
        Scaffold(modifier = modifier.imePadding(),
            topBar = { CreateAppBar(useDarkTheme, backAction) },
            contentWindowInsets = ScaffoldDefaults.contentWindowInsets,
            content = { contentPadding ->
                val state = viewState
                if (!state.isLoading) {
                    Column(
                        modifier = Modifier
                            .padding(contentPadding)
                            .verticalScroll(rememberScrollState())
                    ) {
                        CreationForm(
                            trackingCode = formResult.trackingCode,
                            onCodeChange = {
                                formResult = formResult.copy(trackingCode = it)
                                validationError = ""
                            },
                            name = formResult.parcelName,
                            onNameChange = { formResult = formResult.copy(parcelName = it) },
                            stance = formResult.stance,
                            onStanceChange = { formResult = formResult.copy(stance = it) },
                            notify = formResult.enableNotifications,
                            onNotifyChange = { formResult = formResult.copy(enableNotifications = it) },
                            onScanClicked = {
                                barcode.launch(
                                    ScanOptions()
                                        .setOrientationLocked(false)
                                        .setBarcodeImageEnabled(true)
                                )
                            },
                            onOk = {
                                addParcel(viewModel = viewModel, formResult = formResult, onValidationError = validationAction)
                            },
                            error = validationError
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center), color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier.imePadding(),
                    onClick = {
                        addParcel(viewModel = viewModel, formResult = formResult, onValidationError = validationAction)
                    }
                ) {
                    Icon(imageVector = Icons.Filled.Check, contentDescription = "Ok")
                }
            })
    }
}

private fun addParcel(
    viewModel: CreateParcelViewModel,
    formResult: Form,
    onValidationError: () -> Unit
) {
    if (formResult.trackingCode.isEmpty()) {
        onValidationError()
    } else {
        viewModel.addParcel(
            LocalParcelReference(
                code = UUID.randomUUID().toString(),
                trackingCode = formResult.trackingCode,
                parcelName = formResult.parcelName,
                stance = formResult.stance,
                ultimoEstado = null,
                notify = formResult.enableNotifications,
                updateStatus = LocalParcelReference.UpdateStatus.UNKNOWN
            )
        )
    }
}


@ExperimentalComposeUiApi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreationForm(
    trackingCode: String,
    onCodeChange: (String) -> Unit,
    name: String,
    onNameChange: (String) -> Unit,
    stance: LocalParcelReference.Stance,
    onStanceChange: (LocalParcelReference.Stance) -> Unit,
    notify: Boolean,
    onNotifyChange: (Boolean) -> Unit,
    onScanClicked: () -> Unit,
    onOk: () -> Unit,
    error: String
) {


    val focusManager = LocalFocusManager.current

    CodeInput(focusManager, trackingCode, onCodeChange, onScanClicked, error)
    NameInput(focusManager, name, onNameChange, onOk)
    NotificationsInput(notify, onNotifyChange)
    StanceInput(stance, onStanceChange)
}

@Parcelize
data class Form(
    val trackingCode: String,
    val parcelName: String,
    val enableNotifications: Boolean,
    val stance: LocalParcelReference.Stance
) : Parcelable
