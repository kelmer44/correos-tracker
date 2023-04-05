package net.kelmer.correostracker.list.compose

import android.graphics.Color
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.kelmer.correostracker.dataApi.model.local.LocalParcelReference
import net.kelmer.correostracker.list.ParcelListViewModel
import net.kelmer.correostracker.list.R
import java.text.SimpleDateFormat

@Composable
fun ParcelsScreen(
    state: ParcelListViewModel.State,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            ParcelsAppBar(
                listOf(
                    ActionItem(stringResource(R.string.search), icon = Icons.Filled.Search, order = 0, action = {}),
                    ActionItem(stringResource(R.string.refresh_all), order = 1, action = {}),
                    ActionItem(stringResource(R.string.menu_theme), order = 2, action = {}),
                    ActionItem(stringResource(R.string.about), order = 3, action = {}),
                )
            )
        },
        floatingActionButton = {
            AddParcelFAB()
        }
    ) {
        it
        Column(modifier = modifier.statusBarsPadding()) {
            if (state.list != null) {
                ParcelList(state.list)
            }
            if (state.loading) {

            }
            if (state.error != null) {

            }
        }

    }

}

@Composable
fun ParcelList(list: List<LocalParcelReference>) {
    LazyColumn {
        items(items = list, key = { it.code }) { parcel ->
            ParcelListItem(parcel = parcel) {
            }
        }
    }
}

@Composable
fun ParcelListItem(
    parcel: LocalParcelReference,
    modifier: Modifier = Modifier,
    navigateToDetail: (String) -> Unit
) {
    Card(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .clickable { navigateToDetail(parcel.code) },
        elevation = 2.dp

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = parcel.parcelName.uppercase(),
                        style = MaterialTheme.typography.body1,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = parcel.trackingCode, style = MaterialTheme.typography.body1)
                }
                val (isExpanded, setExpanded) = remember { mutableStateOf(false) }
                OverflowMenuAction(
                    expanded = isExpanded, setExpanded = setExpanded, options =
                    listOf(
                        ActionItem(stringResource(id = R.string.menu_enable_notifications), order = 0, action = {}),
                        ActionItem(stringResource(id = R.string.delete), order = 1, action = {})
                    )
                )

            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(
                        when (parcel.stance) {
                            LocalParcelReference.Stance.INCOMING -> {
                                R.string.incoming
                            }
                            LocalParcelReference.Stance.OUTGOING -> {
                                R.string.outgoing
                            }
                        }
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.caption,
                    fontStyle = FontStyle.Italic,
                    fontSize = 12.sp
                )
            }
            Divider(
                color = androidx.compose.ui.graphics.Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
            Row {
                Column(Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Favorite",
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = parcel.ultimoEstado?.desTextoResumen ?: "",
                        style = MaterialTheme.typography.body2,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = dateFormat.format(parcel.lastChecked),
                        style = MaterialTheme.typography.caption,
                        fontStyle = FontStyle.Italic,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}


private val dateFormat = SimpleDateFormat("dd/MM/yyy HH:mm:ss")
