package net.kelmer.correostracker.detail.compose

import android.app.Activity
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import net.kelmer.correostracker.details.R
import net.kelmer.correostracker.ui.theme.CorreosTheme
import net.kelmer.correostracker.util.copyToClipboard
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract


@Composable
fun BarCodeDialog(
    trackingCode: String,
    barcode: Bitmap? = null,
    extraInfo: ExtraInfo = ExtraInfo.EMPTY,
    onDismissAction: () -> Unit
) {
    val context = LocalContext.current
    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        shape = MaterialTheme.shapes.medium,
        onDismissRequest = onDismissAction,
        title = { Text(text = stringResource(id = R.string.parcel_info)) },
        text = {
            Column() {
                if (barcode != null) {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        bitmap = barcode.asImageBitmap(),
                        contentDescription = stringResource(id = R.string.barcode)
                    )
                }

                Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                    IconButton(onClick = { context.copyToClipboard(trackingCode) }) {
                        Icon(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .size(24.dp),
                            painter = painterResource(id = R.drawable.ic_copy),
                            contentDescription = stringResource(
                                id = R.string.copy
                            ),
                        )
                    }
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(4.dp),
                        textAlign = TextAlign.Center,
                        text = trackingCode,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )
                    ExtraInfoPanel(
                        extraInfo = extraInfo,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismissAction) {
                Text(stringResource(id = android.R.string.ok))
            }
        })
}

@Composable
fun ExtraInfoPanel(
    extraInfo: ExtraInfo = ExtraInfo.EMPTY,
    modifier: Modifier = Modifier
) {
    if (extraInfo.isEmpty().not()) {
        val string = buildString {
            append("(")
            if (!extraInfo.alto.isNullOrZero() &&
                !extraInfo.ancho.isNullOrZero() &&
                !extraInfo.largo.isNullOrZero()
            ) {
                append("${extraInfo.largo}x${extraInfo.ancho}x${extraInfo.alto}cm")
            }
            if (extraInfo.peso.isNullOrZero().not()) {
                if (this.length > 1) {
                    append(", ")
                }
                append("${extraInfo.peso}gr")
            }
            append(")")
        }
        Text(
            text = string,
            modifier = modifier
        )
    }
}

data class ExtraInfo(
    val peso: String? = null,
    val ancho: String? = null,
    val alto: String? = null,
    val largo: String? = null
) {
    fun isEmpty() = peso.isNullOrZero() &&
        ancho.isNullOrZero() &&
        alto.isNullOrZero() &&
        largo.isNullOrZero()

    companion object {
        val EMPTY = ExtraInfo()
    }
}

@OptIn(ExperimentalContracts::class)
inline fun CharSequence?.isNullOrZero(): Boolean {
    contract {
        returns(false) implies (this@isNullOrZero != null)
    }
    return this.isNullOrEmpty() || this == "0"
}

@Composable
@Preview(
    widthDp = 600,

    device = Devices.NEXUS_6
)
fun previewDialog() {
    CorreosTheme() {
        BarCodeDialog("12345", extraInfo = ExtraInfo("12")) {

        }
    }
}
