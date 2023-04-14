package net.kelmer.correostracker.create.compose

import android.view.KeyEvent.ACTION_DOWN
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.kelmer.correostracker.create.CreateParcelViewModel
import net.kelmer.correostracker.create.R
import net.kelmer.correostracker.dataApi.model.local.LocalParcelReference
import net.kelmer.correostracker.ui.compose.NoSearchAppBar
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CreateScreen(
    state: CreateParcelViewModel.State,
    backAction: () -> Unit = {},
    confirmAction: (Form) -> Unit = {}
) {
    Scaffold(
        topBar = { CreateAppBar(backAction) },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets,
        content = { contentPadding ->
            Column(
                modifier = Modifier
                    .padding(contentPadding)
            ) {
                CreationForm()
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { confirmAction }) {
                Icon(imageVector = Icons.Filled.Check, contentDescription = "Ok")
            }
        })
}

@ExperimentalComposeUiApi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreationForm() {
    var formResult by remember { mutableStateOf(Form("", "" , true, LocalParcelReference.Stance.INCOMING)) }

    val radioOptions =
        listOf(
            LocalParcelReference.Stance.INCOMING to stringResource(id = R.string.incoming),
            LocalParcelReference.Stance.OUTGOING to stringResource(id = R.string.outgoing)
        )
    val focusManager = LocalFocusManager.current
    val (code, name, switch, incoming, outgoing) = FocusRequester.createRefs()

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
        value = formResult.trackingCode,
        onValueChange = { value: String -> formResult = formResult.copy(trackingCode = value) },
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
        value = formResult.parcelName,
        onValueChange = { value: String -> formResult = formResult.copy(parcelName = value) },
        label = {
            Text(stringResource(id = R.string.parcel_name))
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    )

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = stringResource(id = R.string.parcel_status_alerts),
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        )
        Switch(
            checked = formResult.enableNotifications,
            onCheckedChange = { formResult = formResult.copy(enableNotifications = it) },
            modifier = Modifier
        )
    }
    Text(
        text = stringResource(id = R.string.stance).uppercase(),
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        radioOptions.forEach { (stance, text) ->
            Row(Modifier
                .selectable(
                    selected = ( stance == formResult.stance),
                    onClick = {
                        formResult = formResult.copy(stance = stance)
                    },
                )) {
                RadioButton(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    selected = ( stance == formResult.stance),
                    onClick = {
                        formResult = formResult.copy(stance = stance)
                    }
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
fun CreateAppBar(
    backAction: () -> Unit = {}
) {
    NoSearchAppBar(
        title = stringResource(id = R.string.add_parcel),
        actionItems = emptyList(),
        navigationIcon = {
            IconButton(onClick = backAction) {
                Icon(Icons.Filled.ArrowBack, "backIcon", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    )
}


@Composable
@Preview
fun CreateScreenPreview() {
    CreateScreen(
        CreateParcelViewModel.State(
        )
    )
}


data class Form(
    val trackingCode: String,
    val parcelName: String,
    val enableNotifications: Boolean,
    val stance: LocalParcelReference.Stance
)
