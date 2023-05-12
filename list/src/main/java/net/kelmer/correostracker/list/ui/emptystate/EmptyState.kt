package net.kelmer.correostracker.list.ui.emptystate

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.kelmer.correostracker.list.R
import net.kelmer.correostracker.ui.compose.CircledIcon


@Composable
fun EmptyState(filter: String?, onAddParcel: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        val emptyStateString = if(filter.isNullOrBlank()) R.string.emptystate_parcel_list else R.string.noresults
        Column(modifier = Modifier.align(Alignment.Center)) {
            CircledIcon(
                bgColor = MaterialTheme.colorScheme.secondary,
                icon = R.drawable.ic_package,
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        onAddParcel()
                    }
            )
            Text(
                text = stringResource(id = emptyStateString),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
@Preview
fun emptyStateFilter() {
    EmptyState("sf")
}
@Composable
@Preview
fun emptyState() {
    EmptyState("")
}
