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
import net.kelmer.correostracker.ui.compose.NoSearchAppBar
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CreateScreen(
    state: CreateParcelViewModel.State,
    backAction: () -> Unit = {}
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
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.Check, contentDescription = "Ok")
            }
        })
}

@ExperimentalComposeUiApi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreationForm() {
    var trackingCode by rememberSaveable { mutableStateOf("") }
    var parcelName by rememberSaveable { mutableStateOf("") }
    val enableNotifications = remember { mutableStateOf(true) }
    val radioOptions = listOf(stringResource(id = R.string.incoming), stringResource(id = R.string.outgoing))
    val (selectedOption, onOptionSelected) = remember {
        mutableStateOf(radioOptions[1])
    }
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
        value = trackingCode,
        onValueChange = { value: String -> trackingCode = value },
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
        value = parcelName,
        onValueChange = { value: String -> parcelName = value },
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
            checked = enableNotifications.value,
            onCheckedChange = { enableNotifications.value = it },
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

        radioOptions.forEach { text ->
            Row(Modifier
                .selectable(
                    selected = (text == selectedOption),
                    onClick = {
                        onOptionSelected(text)
                    },
                )) {
                RadioButton(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    selected = (text == selectedOption),
                    onClick = { onOptionSelected(text) }
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
