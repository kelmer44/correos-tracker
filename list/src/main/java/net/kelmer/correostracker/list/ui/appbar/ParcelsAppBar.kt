@file:OptIn(ExperimentalMaterial3Api::class)

package net.kelmer.correostracker.list.ui.appbar

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.kelmer.correostracker.list.R
import net.kelmer.correostracker.ui.compose.ActionItem
import net.kelmer.correostracker.ui.compose.NoSearchAppBar
import net.kelmer.correostracker.ui.theme.CorreosTheme

@Composable
fun ParcelsAppBar(
    isPremium: Boolean,
    isBillingAvailable: Boolean,
    useDarkTheme: Boolean,
    onTextChange: (String) -> Unit,
    onRefreshAll: () -> Unit = {},
    onThemeClicked: () -> Unit = {},
    onAboutClicked: () -> Unit = {},
    onPremiumClicked: () -> Unit = {}
) {

    var searchWidgetState by remember {
        mutableStateOf(SearchWidgetState.CLOSED)
    }
    Crossfade(targetState = searchWidgetState) { searchState ->
        when (searchState) {
            SearchWidgetState.OPEN -> {
                SearchAppBar(
                    useDarkTheme = useDarkTheme,
                    onTextChange = onTextChange,
                    onCloseClicked = { searchWidgetState = SearchWidgetState.CLOSED },
                    onSearchClicked = {},
                )
            }

            SearchWidgetState.CLOSED -> {
                NoSearchAppBar(
                    withPremiumBadge = isPremium,
                    useDarkTheme = useDarkTheme,
                    title = stringResource(id = R.string.app_name),
                    actionItems = listOfNotNull(
                        ActionItem(stringResource(R.string.search),
                            icon = Icons.Filled.Search,
                            action = {
                            searchWidgetState = SearchWidgetState.OPEN
                        }),
                        ActionItem(stringResource(R.string.refresh_all), action = onRefreshAll),
                        ActionItem(stringResource(R.string.menu_theme), action = onThemeClicked),
                        ActionItem(
                            painterIcon = painterResource(id = R.drawable.ic_medal),
                            name = stringResource(R.string.menu_premium),
                            action = onPremiumClicked
                        ).takeIf { !isPremium && isBillingAvailable },
                        ActionItem(stringResource(R.string.about), action = onAboutClicked),
                    )
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar(
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
    useDarkTheme: Boolean
) {
    val focusRequester = remember { FocusRequester() }
    var textState by remember { mutableStateOf("") }
    val surfaceColor = if (useDarkTheme) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primary
    val elementsColor =
        if (useDarkTheme) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSecondary

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        color = surfaceColor
    ) {


        fun innerTextChange(text: String) {
            textState = text
            onTextChange(text)
        }
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onGloballyPositioned {
                    focusRequester.requestFocus() // IMPORTANT
                },
            value = textState,
            onValueChange = ::innerTextChange,
            placeholder = {
                Text(text = stringResource(R.string.search_placeholder))
            },
            textStyle = TextStyle(
                fontSize = MaterialTheme.typography.bodyMedium.fontSize
            ),
            singleLine = true,
            leadingIcon = {
                Row {
                    IconButton(onClick = {
                        innerTextChange("")
                        onCloseClicked()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(id = R.string.search),
                        )
                    }
                }
            },
            trailingIcon = {
                IconButton(onClick = { innerTextChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Icon",
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
                textColor = elementsColor,
                containerColor = Color.Transparent,
                cursorColor = elementsColor,
                focusedIndicatorColor = Color.Transparent,
                focusedTrailingIconColor = elementsColor,
                focusedLeadingIconColor = elementsColor,
                unfocusedIndicatorColor = elementsColor,
                unfocusedTrailingIconColor = elementsColor,
                focusedLabelColor = elementsColor,
                placeholderColor = elementsColor
            ),
        )
    }
}
@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
fun NoSearchBarPreview() {
    CorreosTheme(false) {
        NoSearchAppBar(
            title = "Seguimiento de correos",
            actionItems = listOf(),
            useDarkTheme = false,
            withPremiumBadge = true
        )
    }
}
@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
fun SearchBarPreview() {
    CorreosTheme(false) {
        SearchAppBar(
            onTextChange = {},
            onCloseClicked = {},
            onSearchClicked = {},
            useDarkTheme = false
        )
    }
}
