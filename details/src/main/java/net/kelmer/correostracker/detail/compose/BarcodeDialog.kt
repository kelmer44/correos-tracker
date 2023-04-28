package net.kelmer.correostracker.detail.compose

import android.app.Activity
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import net.kelmer.correostracker.details.R
import net.kelmer.correostracker.ui.theme.CorreosTheme
import net.kelmer.correostracker.util.copyToClipboard


@Composable
fun BarCodeDialog(
    trackingCode: String,
    barcode: Bitmap? = null,
    onDismissAction: () -> Unit
) {
    val context = LocalContext.current
    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        shape = MaterialTheme.shapes.medium,
        onDismissRequest = onDismissAction,
        title = { Text(text = stringResource(id = R.string.parcel_info)) },
        text = {
            Column {
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
@Preview
fun previewDialog(){
    CorreosTheme() {
        BarCodeDialog("12345") {

        }
    }
}
