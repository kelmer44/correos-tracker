@file:OptIn(ExperimentalMaterial3Api::class)

package net.kelmer.correostracker.list.compose

import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
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
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
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
    val focusRequester = remember { FocusRequester() }

    var textState by remember {
        mutableStateOf("")
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        color = MaterialTheme.colorScheme.primary
    ) {
        fun innerTextChange(text: String) {
            textState = text
            onTextChange(text)
        }

        TextField(
            modifier = Modifier.fillMaxWidth().focusRequester(focusRequester)
                .onGloballyPositioned {
                    focusRequester.requestFocus() // IMPORTANT
                },
            value = textState,
            onValueChange = {
                innerTextChange(it)
            },
            placeholder = {
                Text( text = "Search here...", color = Color.White
                )
            },
            textStyle = TextStyle(
                fontSize = MaterialTheme.typography.bodySmall.fontSize
            ),
            singleLine = true,
            leadingIcon = {
                Row {
                    IconButton(onClick = {
                        innerTextChange("")
                        onCloseClicked() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Search Icon",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Search, contentDescription = "Search Icon", tint = Color.White
                        )
                    }
                }
            },
            trailingIcon = {
                IconButton(onClick = { innerTextChange("") }) {
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
                containerColor = Color.Transparent, cursorColor = Color.White
            ),
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
