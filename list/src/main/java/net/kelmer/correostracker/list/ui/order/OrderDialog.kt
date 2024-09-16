package net.kelmer.correostracker.list.ui.order

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import net.kelmer.correostracker.list.R
import net.kelmer.correostracker.ui.compose.CorreosDialog
import net.kelmer.correostracker.ui.compose.TextRadioButton
import net.kelmer.correostracker.ui.theme.CorreosTheme

@Composable
fun SortDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    onSelect: (SortOption) -> Unit = {},
){
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(SortOption.NAME) }
    CorreosDialog(
        modifier = modifier,
        title = stringResource(id = R.string.order_by_title),
        onDismiss = onDismiss,
        onConfirm =  {
            onSelect(selectedOption)
            onDismiss()
        }
    ) {
        Column(modifier = modifier.selectableGroup()) {
            SortOption.values().forEach { mode ->
                TextRadioButton(
                    modifier = Modifier.fillMaxWidth(),
                    item = mode,
                    selected = (mode == selectedOption),
                    onOptionSelected = { onOptionSelected(mode)},
                    text = stringResource(id = mode.stringRes)
                )
            }
        }
    }
}


@Composable
@Preview
fun SortDialogPreview() {
    CorreosTheme {
        SortDialog()
    }
}
