package net.kelmer.correostracker.list.compose

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import net.kelmer.correostracker.list.R

@Composable
internal fun OverflowMenuAction(
    expanded: Boolean, setExpanded: (Boolean) -> Unit, options: List<ActionItem>
) {
    IconButton(onClick = { setExpanded(true) }) {
        Icon(imageVector = Icons.Filled.MoreVert, contentDescription = stringResource(id = R.string.see_more))
        DropdownMenu(
            expanded = expanded, onDismissRequest = { setExpanded(false) }, offset = DpOffset(x = 0.dp, y = 4.dp)
        ) {
            options.forEach { option ->
                DropdownMenuItem(onClick = {
                    option.action()
                    setExpanded(false)
                }, text = { Text(text = option.name) })
            }
        }
    }
}
