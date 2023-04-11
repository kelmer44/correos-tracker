package net.kelmer.correostracker.detail.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.kelmer.correostracker.details.R
import net.kelmer.correostracker.dataApi.model.remote.CorreosApiEvent

import net.kelmer.correostracker.detail.ParcelDetailViewModel
import net.kelmer.correostracker.ui.compose.ActionItem
import net.kelmer.correostracker.ui.compose.FaseIcon
import net.kelmer.correostracker.ui.compose.NoSearchAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    state: ParcelDetailViewModel.State,
    modifier: Modifier = Modifier,
    backAction: () -> Unit = {},
    onRefresh: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            DetailAppBar(state, backAction, onRefresh)
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets,
        content = { contentPadding ->
            Column(
                modifier = modifier
                    .padding(contentPadding)
            ) {
                if (state.isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
                if (state.parcelDetail != null) {
                    EventList(
                        state.parcelDetail.states,
                    )
                }
                if (state.error != null) {

                }
            }
        }
    )
}

@Composable
fun EventList(events: List<CorreosApiEvent>) {
    if (events.isNotEmpty()) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            itemsIndexed(items = events) { index, event ->
                if (event.desTextoResumen.isNullOrEmpty().not()) {
                    Event(event = event, isFirst = index == 0, isLast = index == events.size - 1)
                }
            }
        }
    } else {
        TODO("Empty state")
    }
}


@Composable
fun Event(
    event: CorreosApiEvent,
    isFirst: Boolean,
    isLast: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 4.dp)
        ) {
            if (!isFirst) {
                //Vertical line, first half
                Divider(
                    color = colorResource(id = R.color.primary),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxHeight(0.5f)
                        .width(3.dp)
                )
            }
            if (!isLast) {
                Divider(
                    color = colorResource(id = R.color.primary),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxHeight(0.5f)
                        .width(3.dp)
                )
            }
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(4.dp)
            ) {
                FaseIcon(faseString = event.fase, modifier = Modifier.align(Alignment.Center))
            }
        }
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(
                    horizontal = 8.dp,
                    vertical = 8.dp
                ),
            shape = RoundedCornerShape(4.dp),
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Text(
                    text = event.fecEvento,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 12.sp
                )
                Text(
                    text = event.horEvento,
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 12.sp
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Text(
                    text = event.desTextoResumen ?: "",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Text(
                    text = event.desTextoAmpliado ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 12.sp
                )
            }
            if (event.unidad.isNullOrBlank().not()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Icon(
                        painter = painterResource(
                            id = R.drawable.ic_location_on_black_24dp
                        ),
                        modifier = Modifier
                            .size(16.dp),
                        contentDescription = "",
                        tint = colorResource(id = R.color.primary)
                    )
                    Text(
                        text = event.unidad ?: "",
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp),
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 12.sp
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
            )
        }
    }
}


@Composable
@Preview
fun EventPreview() {
    Event(
        event = CorreosApiEvent(
            fecEvento = "19/03/2018",
            codEvento = "P040000V",
            horEvento = "16:32",
            fase = "2",
            desTextoResumen = "Clasificado",
            desTextoAmpliado = "Envío clasificado en Centro Logístico",
            unidad = "CTA SANTIAGO DE COMPOSTELA"
        ),
        isFirst = true,
        isLast = false
    )
}

@Composable
@Preview
fun EventListSample() {
    EventList(
        listOf(
            CorreosApiEvent(
                fecEvento = "19/03/2018",
                codEvento = "P040000V",
                horEvento = "16:32",
                fase = "2",
                desTextoResumen = "Clasificado",
                desTextoAmpliado = "Envío clasificado en Centro Logístico",
                unidad = "CTA SANTIAGO DE COMPOSTELA"
            ),
            CorreosApiEvent(
                fecEvento = "19/03/2018",
                codEvento = "P040000V",
                horEvento = "16:32",
                fase = "4",
                desTextoResumen = "Entregado",
                desTextoAmpliado = "Envío clasificado en Centro Logístico",
                unidad = "CTA SANTIAGO DE COMPOSTELA"
            )
        )
    )
}


@Composable
fun DetailAppBar(
    state: ParcelDetailViewModel.State,
    backAction: () -> Unit = {},
    onRefresh: () -> Unit = {}
) {
    NoSearchAppBar(
        title = state.parcelDetail?.name ?: state.trackingCode,
        subtitle = state.parcelDetail?.code,
        navigationIcon = {
            IconButton(onClick = backAction) {
                Icon(Icons.Filled.ArrowBack, "backIcon", tint = MaterialTheme.colorScheme.onPrimary)
            }
        },
        actionItems = listOf(
            ActionItem(
                stringResource(id = R.string.refresh),
                icon = Icons.Filled.Refresh,
                action = onRefresh
            )
        )
    )
}
