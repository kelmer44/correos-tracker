package net.kelmer.correostracker.list.compose.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import net.kelmer.correostracker.list.R
import net.kelmer.correostracker.list.themedialog.ThemeMode
import net.kelmer.correostracker.ui.compose.CorreosDialog

@Composable
fun ThemeDialog(onDismiss: () -> Unit,
                onSelect: (ThemeMode) -> Unit,
                modifier: Modifier = Modifier) {
    CorreosDialog(
        title = stringResource(id = R.string.theme_title),
        onDismiss = onDismiss,
        onConfirm = onDismiss
    ) {
        Column(modifier = modifier) {
            ThemeMode.values().map {
                TextButton(
                    shape = AbsoluteCutCornerShape(0.dp),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onSelect(it)
                        onDismiss()
                    }) {
                    Text(text = stringResource(id = it.stringRes), modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start)
                }
            }
        }
    }
}
