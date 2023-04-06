package net.kelmer.correostracker.list.compose

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import net.kelmer.correostracker.dataApi.model.local.LocalParcelReference
import net.kelmer.correostracker.fase.Fase
import net.kelmer.correostracker.list.ParcelListViewModel
import net.kelmer.correostracker.list.R
import net.kelmer.correostracker.ui.theme.CorreosTheme
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParcelsScreen(
    state: ParcelListViewModel.State,
    modifier: Modifier = Modifier,
    onTextChange: (String) -> Unit
) {
    Scaffold(
        topBar = {
            ParcelsAppBar(onTextChange)
        },
        floatingActionButton = {
            AddParcelFAB()
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets,
        content = { contentPadding ->
            Column(modifier = modifier.padding(contentPadding)) {
                if (state.list != null) {
                    ParcelList(state.list)
                }
                if (state.loading) {

                }
                if (state.error != null) {

                }
            }
        }
    )
}

@Composable
fun ParcelList(list: List<LocalParcelReference>) {
    LazyColumn {
        items(items = list, key = { it.code }) { parcel ->
            ParcelListItem(parcel = parcel) {}
        }
    }
}

@Composable
fun ParcelListItem(
    parcel: LocalParcelReference,
    modifier: Modifier = Modifier,
    navigateToDetail: (String) -> Unit
) {
    ElevatedCard(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .clickable { navigateToDetail(parcel.code) },
        shape = RoundedCornerShape(4.dp),
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
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = parcel.trackingCode, style = MaterialTheme.typography.labelLarge)
                }
                val (isExpanded, setExpanded) = remember { mutableStateOf(false) }
                OverflowMenuAction(
                    expanded = isExpanded, setExpanded = setExpanded, options =
                    listOf(
                        ActionItem(
                            stringResource(id = R.string.menu_enable_notifications),
                            action = {}),
                        ActionItem(stringResource(id = R.string.delete), action = {})
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
                    style = MaterialTheme.typography.bodySmall,
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
                val faseRaw = parcel.ultimoEstado?.fase
                val faseNumber: Int? = if (faseRaw == "?") null else faseRaw?.toIntOrNull()
                val fase = if (faseNumber != null) Fase.fromFase(faseNumber) else Fase.OTHER
                val bitmap = ContextCompat.getDrawable(LocalContext.current, fase.drawable)?.toBitmap()
                    ?.asImageBitmap()!!
                Column(Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
                    Icon(
                        bitmap = bitmap,
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
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = dateFormat.format(parcel.lastChecked),
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}


private
val dateFormat = SimpleDateFormat("dd/MM/yyy HH:mm:ss")
