package net.kelmer.correostracker.list.ui.sort

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.kelmer.correostracker.list.ParcelSort
import net.kelmer.correostracker.list.ParcelSortOrder
import net.kelmer.correostracker.list.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortBar(
    order: ParcelSortOrder,
    onSortSelected: (ParcelSort) -> Unit,
    onToggleDirection: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.sort_label),
            style = MaterialTheme.typography.labelLarge
        )
        FilterChip(
            selected = order.sort == ParcelSort.NAME,
            onClick = { onSortSelected(ParcelSort.NAME) },
            label = { Text(stringResource(R.string.sort_name)) }
        )
        FilterChip(
            selected = order.sort == ParcelSort.DATE,
            onClick = { onSortSelected(ParcelSort.DATE) },
            label = { Text(stringResource(R.string.sort_date)) }
        )
        Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.End) {
            IconButton(onClick = onToggleDirection) {
                Icon(
                    painter = painterResource(
                        if (order.ascending) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down
                    ),
                    contentDescription = stringResource(
                        if (order.ascending) R.string.sort_ascending else R.string.sort_descending
                    )
                )
            }
        }
    }
}

@Preview
@Composable
private fun SortBarPreview() {
    SortBar(ParcelSortOrder(ParcelSort.DATE, ascending = false), {}, {})
}
