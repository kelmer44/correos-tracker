package net.kelmer.correostracker.list.compose

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import net.kelmer.correostracker.list.R

@Composable
fun AddParcelFAB() {
    FloatingActionButton(onClick = { /*TODO*/ }) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(id = R.string.add_parcel)
        )
    }
}
