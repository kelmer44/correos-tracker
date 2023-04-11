package net.kelmer.correostracker.create.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import net.kelmer.correostracker.create.R
import net.kelmer.correostracker.ui.compose.NoSearchAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen(
    backAction: () -> Unit = {}
) {
    Scaffold(
        topBar = { CreateAppBar(backAction) },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets,
        content = { contentPadding ->
            Column(
                modifier = Modifier
                    .padding(contentPadding)
            ) {

            }

        })
}

@Composable
fun CreateAppBar(
    backAction: () -> Unit = {}
) {
    NoSearchAppBar(
        title = stringResource(id = R.string.add_parcel),
        actionItems = emptyList(),
        navigationIcon = {
            IconButton(onClick = backAction) {
                Icon(Icons.Filled.ArrowBack, "backIcon", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    )
}


@Composable
@Preview
fun CreateScreenPreview() {
    CreateScreen()
}
