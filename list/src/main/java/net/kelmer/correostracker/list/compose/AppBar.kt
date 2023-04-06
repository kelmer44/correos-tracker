package net.kelmer.correostracker.list.compose

import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.kelmer.correostracker.list.R
import timber.log.Timber


@Composable
fun ParcelsAppBar(
    onTextChange: (String) -> Unit
) {

    var searchWidgetState by remember {
        mutableStateOf(SearchWidgetState.CLOSED)
    }
    Crossfade(targetState = searchWidgetState) { searchState ->
        when (searchState) {
            SearchWidgetState.OPEN -> {
                SearchAppBar(
                    onTextChange = onTextChange,
                    onCloseClicked = { searchWidgetState = SearchWidgetState.CLOSED },
                    onSearchClicked = {},
                )
            }
            SearchWidgetState.CLOSED -> {
                NoSearchAppBar(
                    listOf(
                        ActionItem(stringResource(R.string.search), icon = Icons.Filled.Search, action = {
                            searchWidgetState = SearchWidgetState.OPEN
                        }),
                        ActionItem(stringResource(R.string.refresh_all), action = {}),
                        ActionItem(stringResource(R.string.menu_theme), action = {}),
                        ActionItem(stringResource(R.string.about), action = {}),
                    )
                )
            }
        }
    }
}

@Composable
fun NoSearchAppBar(
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
            if (options.isNotEmpty()) {
                val (isExpanded, setExpanded) = remember { mutableStateOf(false) }
                OverflowMenuAction(isExpanded, setExpanded, options)
            }
        },
    )
}

@Composable
fun SearchAppBar(
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit
) {

    var textState by remember {
        mutableStateOf("")
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        elevation = AppBarDefaults.TopAppBarElevation,
        color = MaterialTheme.colors.primary
    ) {
        fun innerTextChange(text: String) {
            textState = text
            onTextChange(text)
        }

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = textState,
            onValueChange = {
                innerTextChange(it)
            },
            placeholder = {
                Text(
                    modifier = Modifier.alpha(ContentAlpha.medium), text = "Search here...", color = Color.White
                )
            },
            textStyle = TextStyle(
                fontSize = MaterialTheme.typography.subtitle1.fontSize
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(modifier = Modifier.alpha(ContentAlpha.medium), onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.Search, contentDescription = "Search Icon", tint = Color.White
                    )
                }
            },
            trailingIcon = {
                IconButton(onClick = {
                    if (textState.isNotEmpty()) {
                        innerTextChange("")
                    } else {
                        onCloseClicked()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Close, contentDescription = "Close Icon", tint = Color.White
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(onSearch = {
                onSearchClicked(textState)
            }),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent, cursorColor = Color.White.copy(alpha = ContentAlpha.medium)
            )
        )
    }


}


@Composable
@Preview
fun AppBarPreview() {
    NoSearchAppBar(
        actionItems = listOf(
            ActionItem(name = "Search", icon = Icons.Filled.Search, action = {}),
            ActionItem("Refresh", action = {})
        )
    )
}

@Composable
@Preview
fun SearchBarPreview() {
    SearchAppBar(onTextChange = {}, onCloseClicked = { }, onSearchClicked = {})
}
