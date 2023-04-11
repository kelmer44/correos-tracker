package net.kelmer.correostracker.detail.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.kelmer.correostracker.details.R
import net.kelmer.correostracker.dataApi.model.remote.CorreosApiEvent

import net.kelmer.correostracker.detail.ParcelDetailViewModel
import net.kelmer.correostracker.ui.compose.FaseIcon
import net.kelmer.correostracker.ui.compose.NoSearchAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    state: ParcelDetailViewModel.State,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar =
        {
            DetailAppBar(state)
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets,
        content = { contentPadding ->
            Column(modifier = modifier.padding(contentPadding)) {
                if (state.isLoading) {

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
        LazyColumn {
            itemsIndexed(items = events) { index, event ->
                if (event.desTextoResumen.isNullOrEmpty().not()) {
                    Event(event = event, isFirst = index == 0, isLast = index == events.size-1)
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
            .height(IntrinsicSize.Min)
    ) {
        Box(modifier = Modifier.fillMaxHeight()) {
            if(!isFirst) {
                //Vertical line, first half
                Divider(
                    color = colorResource(id = R.color.primary),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxHeight(0.5f)
                        .width(4.dp)
                )
            }
            if(!isLast) {
                Divider(
                    color = colorResource(id = R.color.primary),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxHeight(0.5f)
                        .width(4.dp)
                )
            }
            Box(modifier = Modifier
                .align(Alignment.Center)
                .padding(4.dp)) {
                FaseIcon(faseString = event.fase, modifier = Modifier.align(Alignment.Center))
            }
        }
        Column {
            ElevatedCard(
                modifier = Modifier
                    .padding(
                        horizontal = 8.dp, vertical = 8.dp
                    ),
                shape = RoundedCornerShape(4.dp),
            ) {
                Row {
                    Text(text = event.fecEvento, modifier = Modifier.weight(1f))
                    Text(text = event.horEvento)
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(text = event.desTextoResumen ?: "")
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(text = event.desTextoAmpliado ?: "")
                }
                Row() {
                    Icon(
                        painter = painterResource(
                            id = R.drawable.ic_location_on_black_24dp
                        ),
                        contentDescription = ""
                    )
                    Text(text = event.unidad ?: "", modifier = Modifier.weight(1f))
                }
            }
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
fun DetailAppBar(state: ParcelDetailViewModel.State) {
    NoSearchAppBar(
        title = state.parcelDetail?.name ?: "No title",
        listOf()
    )
}
