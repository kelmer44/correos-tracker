package net.kelmer.correostracker.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun <T> TextRadioButton(
    item: T,
    modifier: Modifier = Modifier,
    selected: Boolean,
    onOptionSelected: (T) -> Unit,
    text: String
) {
    Row(
        modifier
            .height(56.dp)
            .selectable(
                selected = selected,
                onClick = { onOptionSelected(item) },
                role = Role.RadioButton
            )
            .padding(horizontal = 16.dp)
    ) {
        RadioButton(
            modifier = Modifier.align(Alignment.CenterVertically),
            selected = selected,
            onClick = null
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(start = 16.dp)
                .align(Alignment.CenterVertically)
        )
    }
}


@Composable
@Preview
fun PreviewTextRadioButton() {
    Column {
        listOf("one", "two", "three").forEachIndexed { index, value ->
            TextRadioButton(item = value, selected = index == 0, onOptionSelected = {}, text = value)
        }
    }
}
