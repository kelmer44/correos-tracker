package net.kelmer.correostracker.create.compose

import android.content.res.Configuration
import android.os.Parcelable
import android.view.KeyEvent.ACTION_DOWN
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava2.subscribeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.parcelize.Parcelize
import net.kelmer.correostracker.create.CreateParcelViewModel
import net.kelmer.correostracker.create.R
import net.kelmer.correostracker.dataApi.model.local.LocalParcelReference
import net.kelmer.correostracker.ui.compose.NoSearchAppBar
import net.kelmer.correostracker.ui.theme.CorreosTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import timber.log.Timber
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CreateScreen(
    modifier: Modifier = Modifier,
    viewModel: CreateParcelViewModel = viewModel(),
    backAction: () -> Unit = {},
) {
    val viewState by viewModel.stateOnceAndStream.subscribeAsState(CreateParcelViewModel.State())

    var formResult by rememberSaveable {
        mutableStateOf(
            Form("", "", true, LocalParcelReference.Stance.INCOMING)
        )
    }

    Scaffold(
        topBar = { CreateAppBar(backAction) },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets,
        content = { contentPadding ->
            val state = viewState
            if(!state.isLoading) {
                Column(
                    modifier = Modifier
                        .padding(contentPadding)
                        .verticalScroll(rememberScrollState())
                ) {
                    CreationForm(
                        trackingCode = formResult.trackingCode,
                        onCodeChange = { formResult = formResult.copy(trackingCode = it) },
                        name = formResult.parcelName,
                        onNameChange = { formResult = formResult.copy(parcelName = it) },
                        stance = formResult.stance,
                        onStanceChange = { formResult = formResult.copy(stance = it) },
                        notify = formResult.enableNotifications,
                        onNotifyChange = { formResult = formResult.copy(enableNotifications = it) }
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
                onClick = {
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
            ) {
                Icon(imageVector = Icons.Filled.Check, contentDescription = "Ok")
            }
        })
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
    onNotifyChange: (Boolean) -> Unit
) {


    val radioOptions =
        listOf(
            LocalParcelReference.Stance.INCOMING to stringResource(id = R.string.incoming),
            LocalParcelReference.Stance.OUTGOING to stringResource(id = R.string.outgoing)
        )
    val focusManager = LocalFocusManager.current

    CodeInput(focusManager, trackingCode, onCodeChange)
    NameInput(focusManager, name, onNameChange)
    NotificationsInput(notify, onNotifyChange)
    StanceInput(radioOptions, stance, onStanceChange)
}

@Composable
private fun StanceInput(
    radioOptions: List<Pair<LocalParcelReference.Stance, String>>,
    stance: LocalParcelReference.Stance,
    onStanceChange: (LocalParcelReference.Stance) -> Unit
) {
    Text(
        text = stringResource(id = R.string.stance).uppercase(),
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        radioOptions.forEach { (thisStance, text) ->
            Row(
                Modifier
                    .selectable(
                        selected = thisStance == stance,
                        onClick = { onStanceChange(thisStance) },
                    )
            ) {
                RadioButton(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    selected = thisStance == stance,
                    onClick = { onStanceChange(thisStance) }
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium.merge(),
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .align(Alignment.CenterVertically)
                )
            }

        }
    }
}

@Composable
private fun NotificationsInput(notify: Boolean, onNotifyChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = stringResource(id = R.string.parcel_status_alerts),
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
                .padding(end = 16.dp)
        )
        Switch(
            checked = notify,
            onCheckedChange = onNotifyChange,
            modifier = Modifier
        )
    }
}

@ExperimentalComposeUiApi
@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun NameInput(
    focusManager: FocusManager,
    name: String,
    onNameChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .onPreviewKeyEvent {
                if (it.key == Key.Tab && it.nativeKeyEvent.action == ACTION_DOWN) {
                    focusManager.moveFocus(FocusDirection.Down)
                    true
                } else {
                    false
                }
            },
        singleLine = true,
        value = name,
        onValueChange = onNameChange,
        label = {
            Text(stringResource(id = R.string.parcel_name))
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    )
}

@ExperimentalComposeUiApi
@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CodeInput(
    focusManager: FocusManager,
    trackingCode: String,
    onCodeChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .onPreviewKeyEvent {
                if (it.key == Key.Tab && it.nativeKeyEvent.action == ACTION_DOWN) {
                    focusManager.moveFocus(FocusDirection.Down)
                    true
                } else {
                    false
                }
            },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        value = trackingCode,
        onValueChange = onCodeChange,
        label = {
            Text(stringResource(id = R.string.parcel_code))
        },
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = {
                Timber.i("Scan")
            }) {
                Icon(painter = painterResource(id = R.drawable.ic_barcode), contentDescription = "Scan barcode")
            }
        },
        supportingText = {
            Text(text = stringResource(id = R.string.code_helper_text))
        }
    )
}

@Composable
fun CreateAppBar(
    backAction: () -> Unit = {}
) {
    NoSearchAppBar(
        title = stringResource(id = R.string.add_parcel),
        actionItems = emptyList(),
        navigationIcon = {
            IconButton(onClick = backAction) {
                Icon(Icons.Filled.ArrowBack, "backIcon")
            }
        }
    )
}

@Parcelize
data class Form(
    val trackingCode: String,
    val parcelName: String,
    val enableNotifications: Boolean,
    val stance: LocalParcelReference.Stance
) : Parcelable
