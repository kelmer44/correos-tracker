package net.kelmer.correostracker.list.compose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import net.kelmer.correostracker.list.R

@Composable
fun AddParcelFAB() {
    ExtendedFloatingActionButton(
        onClick = { /*TODO*/ },
        text = {
            Text(text = stringResource(id = R.string.add_parcel))
        },
        icon = {
            Icon(imageVector = Icons.Filled.Add, contentDescription = stringResource(id = R.string.add_parcel))
        },
        expanded = true
    )
}
