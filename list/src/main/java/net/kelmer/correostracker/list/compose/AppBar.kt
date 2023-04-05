package net.kelmer.correostracker.list.compose

import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import net.kelmer.correostracker.list.R

@Composable
fun ParcelsAppBar(
    actionItems: List<ActionItem>
) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.app_name))
        },
        elevation = 8.dp,
        actions = {
            val (icons, options) = actionItems.partition { it.icon != null }

            icons.forEach {
                IconButton(onClick = it.action) {
                    Icon(imageVector = it.icon!!, contentDescription = it.name)
                }
            }
            val (isExpanded, setExpanded) = remember { mutableStateOf(false) }
            OverflowMenuAction(isExpanded, setExpanded, options)
        },
    )
}
