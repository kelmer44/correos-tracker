package net.kelmer.correostracker.ui.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.kelmer.correostracker.ui.theme.CorreosTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoSearchAppBar(
    withPremiumBadge: Boolean = false,
    useDarkTheme: Boolean,
    title: String,
    actionItems: List<ActionItem>,
    subtitle: String? = null,
    navigationIcon: @Composable () -> Unit = {},
) {
    AppBarTheme(useDarkTheme) {
        val containerColor = if (!useDarkTheme) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
        val itemsColor = if (!useDarkTheme) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

        TopAppBar(
            title = {
                Column {
                    BadgedBox(
                        badge = {
                            if(withPremiumBadge) {
                                Badge(
                                    containerColor =
                                    if(useDarkTheme) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(top = 24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = "",
                                        Modifier
                                            .padding(top = 4.dp, bottom = 4.dp)
                                            .size(8.dp)
                                    )
                                }
                            }
                        },
                    ) {
                        Text(
                            text = title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    if (subtitle != null) {
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.titleSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = containerColor,
                titleContentColor = itemsColor,
                actionIconContentColor = itemsColor,
                navigationIconContentColor = itemsColor
            ),
            actions = {
                val (icons, options) = actionItems.partition { it.icon != null || it.painterIcon != null }

                icons.forEach {
                    if(it.visible) {
                        IconButton(
                            onClick = it.action,
                            enabled = it.enabled
                        ) {
                            if (it.icon != null) {
                                Icon(imageVector = it.icon, contentDescription = it.name)
                            } else if (it.painterIcon != null) {
                                Icon(painter = it.painterIcon, contentDescription = it.name)
                            }
                        }
                    }
                }
                if (options.isNotEmpty()) {
                    val (isExpanded, setExpanded) = remember { mutableStateOf(false) }
                    OverflowMenuAction(isExpanded, setExpanded, options)
                }
            },
            navigationIcon = navigationIcon
        )
    }
}

@Composable
fun AppBarTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit,
) {
    val typography = MaterialTheme.typography.copy(
        titleLarge = TextStyle(
            fontWeight = FontWeight.SemiBold, fontSize = 20.sp, lineHeight = 28.sp, letterSpacing = 0.sp
        ),

        )
    CorreosTheme(
        useDarkTheme = useDarkTheme,
        overrideTypography = typography,
        content = content
    )
}

@Composable
@Preview
fun AppBarPreview() {
    NoSearchAppBar(
        withPremiumBadge = true,
        useDarkTheme = true,
        title = "Seguimiento de correos",
        subtitle = "My subtitle",
        actionItems = listOf(
            ActionItem(name = "Search", icon = Icons.Filled.Search, action = {}),
            ActionItem("Refresh", action = {})
        )
    )
}
