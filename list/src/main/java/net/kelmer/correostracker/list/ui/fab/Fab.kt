package net.kelmer.correostracker.list.ui

import android.content.res.Configuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import net.kelmer.correostracker.list.R
import net.kelmer.correostracker.ui.theme.CorreosTheme

@Composable
fun AddParcelFAB(onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        text = {
            Text(text = stringResource(id = R.string.add_parcel))
        },
        icon = {
            Icon(imageVector = Icons.Filled.Add, contentDescription = stringResource(id = R.string.add_parcel))
        },
        expanded = true,
    )
}


@Composable
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
fun previewFAB() {
    CorreosTheme(true) {
        AddParcelFAB {

        }
    }
}
