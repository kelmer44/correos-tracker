package net.kelmer.correostracker.list.compose.feature

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import net.kelmer.correostracker.list.ParcelListViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import net.kelmer.correostracker.list.R
import net.kelmer.correostracker.ui.compose.CorreosDialog

@Composable
fun FeatureDialog(
    viewModel: ParcelListViewModel = viewModel(),
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onWebClick: () -> Unit
) {

    CorreosDialog(title = stringResource(id = R.string.about), onDismiss = onDismiss, onConfirm = onDismiss) {
        Column(
            modifier = modifier
        ) {
            Column {
                Text(
                    text = stringResource(id = R.string.feature_dialog_opensource),
                    textAlign = TextAlign.Center
                )
                OutlinedButton(
                    onClick = onWebClick,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 8.dp)
                ) {
                    Text(text = stringResource(id = R.string.visit_web))
                }
            }
            Divider(
                thickness = 2.dp
            )
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(4.dp),
                text = stringResource(id = R.string.feature_dialog_title).uppercase(),
                fontWeight = FontWeight.Bold
            )
            LazyColumn(
                modifier = Modifier.height(192.dp)
            ) {
                items(items = viewModel.getFeatureList()) { feature ->
                    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)) {
                        Text(
                            modifier = Modifier.align(Alignment.Start),
                            text = buildAnnotatedString {
                                    append(feature.version)
                            },
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = buildAnnotatedString {
                                val text = stringResource(id = feature.text)
                                append(text)
                            },
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
            Divider(
                modifier = Modifier.padding(top = 8.dp),
                thickness = 2.dp
            )
        }
    }
}