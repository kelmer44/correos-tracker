package net.kelmer.correostracker.ui.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import net.kelmer.correostracker.theme.R

@Composable
fun OverflowMenuAction(
    expanded: Boolean,
    setExpanded: (Boolean) -> Unit,
    options: List<ActionItem>,
    compact: Boolean,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = { setExpanded(true) }, modifier = if(compact) modifier.size(24.dp) else modifier) {
        Icon(
            imageVector = if (compact) Icons.Filled.ArrowDropDown else Icons.Filled.MoreVert,
            contentDescription = stringResource(id = R.string.see_more)
        )
        DropdownMenu(
            expanded = expanded, onDismissRequest = { setExpanded(false) }, offset = DpOffset(x = 0.dp, y = 4.dp)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        option.action()
                        setExpanded(false)
                    },
                    text = { Text(text = option.name) }
                )
            }
        }
    }
}

@Composable
@Preview
fun previewMenu() {
    OverflowMenuAction(expanded = false, setExpanded = {}, options = listOf(ActionItem("Hola")), compact = true)
}
