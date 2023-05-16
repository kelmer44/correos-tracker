package net.kelmer.correostracker.create.compose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import net.kelmer.correostracker.create.R
import net.kelmer.correostracker.ui.compose.NoSearchAppBar
import net.kelmer.correostracker.ui.theme.CorreosTheme

@Composable
fun CreateAppBar(
    useDarkTheme: Boolean = false, backAction: () -> Unit = {}
) {
    NoSearchAppBar(
        useDarkTheme = useDarkTheme,
        title = stringResource(id = R.string.add_parcel),
        actionItems = emptyList(),
        navigationIcon = {
            IconButton(onClick = backAction) {
                Icon(Icons.Filled.ArrowBack, "backIcon")
            }
        }
    )
}

@Composable
@Preview
fun CreateAppBarPreview() {
    CorreosTheme {
        CreateAppBar()
    }
}
