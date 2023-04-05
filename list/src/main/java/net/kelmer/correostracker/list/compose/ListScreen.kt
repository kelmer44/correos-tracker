package net.kelmer.correostracker.list.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import net.kelmer.correostracker.dataApi.model.local.LocalParcelReference
import net.kelmer.correostracker.list.ParcelListViewModel
import net.kelmer.correostracker.list.R
import java.text.SimpleDateFormat


@Composable
fun Parcels(
    state: ParcelListViewModel.State,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier
        .verticalScroll(rememberScrollState())
        .statusBarsPadding()
    ) {
        ParcelsAppBar()
        if(state.list!=null) {
            ParcelList(state.list)
        }
        if(state.loading) {

        }
    }

}

@Composable
fun ParcelList(list: List<LocalParcelReference>) {
    LazyColumn {
        items(items = list, key = { it.code }) {parcel ->
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
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { navigateToDetail(parcel.code) }
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)) {

            Row(modifier = Modifier.fillMaxWidth()) {
                Column( modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = parcel.parcelName, style = MaterialTheme.typography.labelLarge)
                    Text(text = parcel.trackingCode, style = MaterialTheme.typography.labelSmall)
                }
                IconButton(onClick = { /*TODO*/ }) {
                   Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Favorite",
                        tint = MaterialTheme.colorScheme.outline
                    )
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = parcel.stance.name)
            }
            Row {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Favorite",
                    tint = MaterialTheme.colorScheme.outline
                )
                Column(modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.Center) {
                    Text(text = parcel.ultimoEstado?.desTextoResumen ?: "", style = MaterialTheme.typography.labelLarge)
                    Text(text = dateFormat.format(parcel.lastChecked), style = MaterialTheme.typography.labelSmall)
                }
            }

        }
    }
}


private val dateFormat = SimpleDateFormat("dd/MM/yyy HH:mm:ss")

@Composable
fun ParcelsAppBar(){
    TopAppBar(
        elevation = 0.dp,
        modifier = Modifier.height(48.dp)
    ) {
        Text(text = stringResource(id = R.string.app_name))
        IconButton(
            modifier = Modifier.align(Alignment.CenterVertically),
            onClick = {}
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = stringResource(id = R.string.search)
            )
        }
    }
}
