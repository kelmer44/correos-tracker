package net.kelmer.correostracker.ui.compose

import android.R
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun CorreosDialog(
    title: String,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        onDismissRequest = onDismiss,
        title = {
            Text(text = title)
        },
        text = {
            content()
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(stringResource(id = R.string.ok))
            }
        })
}

@Composable
fun ConfirmDialog(
    title: String,
    text: String,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    CorreosDialog(
        title = title,
        onDismiss = onDismiss,
        onConfirm = onConfirm,
        modifier = modifier
    ) {
        Column {
            Text(text = text)
        }
    }
//    AlertDialog(
//        modifier = modifier,
//        shape = MaterialTheme.shapes.medium,
//        onDismissRequest = onDismiss,
//        title = {
//            Text(text = title)
//        },
//        text = {
//            Column {
//                Text(text = text)
//            }
//        },
//        confirmButton = {
//            Button(onClick = onConfirm) {
//                Text(stringResource(id = R.string.ok))
//            }
//        })
}
