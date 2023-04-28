package net.kelmer.correostracker.create.compose

import android.view.KeyEvent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import net.kelmer.correostracker.create.R

@ExperimentalComposeUiApi
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CodeInput(
    focusManager: FocusManager,
    trackingCode: String,
    onCodeChange: (String) -> Unit,
    onScanClicked: () -> Unit,
    error: String
) {
    OutlinedTextField(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)
        .onPreviewKeyEvent {
            if (it.key == Key.Tab && it.nativeKeyEvent.action == KeyEvent.ACTION_DOWN) {
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
            IconButton(onClick = onScanClicked) {
                Icon(painter = painterResource(id = R.drawable.ic_scan_barcode), contentDescription = "Scan barcode")
            }
        },
        supportingText = {
            Text(text = error.ifBlank { stringResource(id = R.string.code_helper_text) })
        },
        isError = error.isNotBlank(),
    )
}
