package net.kelmer.correostracker.ui.compose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import net.kelmer.correostracker.list.compose.OverflowMenuAction
import net.kelmer.correostracker.ui.theme.CorreosTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoSearchAppBar(
    title: String,
    actionItems: List<ActionItem>
) {
    AppBarTheme(false) {
        TopAppBar(
            title = {
                Text(
                    text = title,
                )
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimary
            ),
            actions = {
                val (icons, options) = actionItems.partition { it.icon != null }

                icons.forEach {
                    IconButton(onClick = it.action) {
                        Icon(imageVector = it.icon!!, contentDescription = it.name)
                    }
                }
                if (options.isNotEmpty()) {
                    val (isExpanded, setExpanded) = remember { mutableStateOf(false) }
                    OverflowMenuAction(isExpanded, setExpanded, options)
                }
            },
        )
    }
}

@Composable
fun AppBarTheme(
    useDarkTheme: Boolean,
    content: @Composable() () -> Unit,
) {
    val typography = MaterialTheme.typography.copy(
        titleLarge = TextStyle(
            fontWeight = FontWeight.SemiBold, fontSize = 20.sp, lineHeight = 28.sp, letterSpacing = 0.sp
        )
    )
    CorreosTheme(
        useDarkTheme, overrideTypography = typography, content = content
    )
}

@Composable
@Preview
fun AppBarPreview() {
    NoSearchAppBar(
        title = "My title",
        actionItems = listOf(
            ActionItem(name = "Search", icon = Icons.Filled.Search, action = {}),
            ActionItem("Refresh", action = {})
        )
    )
}
