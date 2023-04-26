package net.kelmer.correostracker.detail.compose

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava2.subscribeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.launch
import net.kelmer.correostracker.dataApi.model.exception.CorreosException
import net.kelmer.correostracker.details.R
import net.kelmer.correostracker.dataApi.model.remote.CorreosApiEvent

import net.kelmer.correostracker.detail.DetailViewModel
import net.kelmer.correostracker.ui.compose.ActionItem
import net.kelmer.correostracker.ui.compose.ErrorView
import net.kelmer.correostracker.ui.compose.FaseIcon
import net.kelmer.correostracker.ui.compose.NoSearchAppBar
import net.kelmer.correostracker.util.NetworkInteractor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    useDarkTheme: Boolean,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = viewModel(),
    backAction: () -> Unit = {},
    copyAction: () -> Unit = {}
) {
    val viewState by viewModel.stateOnceAndStream.subscribeAsState(DetailViewModel.State(""))

    Scaffold(
        topBar = {
            DetailAppBar(useDarkTheme, viewState, backAction, viewModel::refresh)
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets,
        content = { contentPadding ->
            val state = viewState

            Column(modifier = modifier.padding(contentPadding)) {
                if (viewState.isLoading) {
                    Loading()
                }
                if (state.parcelDetail != null) {
                    EventList(
                        state.parcelDetail.states,
                    )
                }
                if (state.error != null) {
                    ErrorMap(state)
                }
            }
        })
}

@Composable
private fun ErrorMap(state: DetailViewModel.State) {
    when (state.error) {
        is CorreosException -> {
            FirebaseCrashlytics.getInstance().log("Controlled Exception Error ${state.trackingCode}")
            FirebaseCrashlytics.getInstance().recordException(state.error)
            ErrorView(
                message = state.error.message ?: "",
            )
        }

        is NetworkInteractor.NetworkUnavailableException -> {
            FirebaseCrashlytics.getInstance()
                .log("Controlled Network Unavailable Exception Error ${state.trackingCode}")
            FirebaseCrashlytics.getInstance().recordException(state.error)
            ErrorView(
                stringResource(
                    id = R.string.error_no_network
                )
            )
        }

        else -> {
            FirebaseCrashlytics.getInstance().log("Unknown Error ${state.trackingCode}")
            if (state.error != null) {
                FirebaseCrashlytics.getInstance().recordException(state.error)
            }
            ErrorView(
                stringResource(
                    id = R.string.error_unrecognized
                )
            )
        }
    }
}

@Composable
private fun Loading() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center), color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun EventList(events: List<CorreosApiEvent>) {
    if (events.isNotEmpty()) {

        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()
        LaunchedEffect(events.size) {
            coroutineScope.launch {
                listState.scrollToItem(index = events.size)
            }
        }
        LazyColumn(
            state = listState,
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
    event: CorreosApiEvent, isFirst: Boolean, isLast: Boolean
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
                    horizontal = 8.dp, vertical = 8.dp
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
                    text = event.horEvento, style = MaterialTheme.typography.bodySmall, fontSize = 12.sp
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Text(
                    text = event.desTextoResumen ?: "", style = MaterialTheme.typography.bodyMedium
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Text(
                    text = event.desTextoAmpliado ?: "", style = MaterialTheme.typography.bodySmall, fontSize = 12.sp
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
                        modifier = Modifier.size(16.dp),
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
        ), isFirst = true, isLast = false
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
            ), CorreosApiEvent(
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
    useDarkTheme: Boolean,
    state: DetailViewModel.State,
    backAction: () -> Unit = {},
    onRefresh: () -> Unit = {},
    copyAction: () -> Unit = {}
) {
    var barCodeShown by rememberSaveable { mutableStateOf(false) }

    NoSearchAppBar(
        useDarkTheme = useDarkTheme,
        title = state.parcelDetail?.name ?: state.trackingCode,
        subtitle = state.parcelDetail?.code,
        navigationIcon = {
            IconButton(onClick = backAction) {
                Icon(Icons.Filled.ArrowBack, "backIcon")
            }
        },
        actionItems = listOf(
            ActionItem(stringResource(id = R.string.parcel_info),
                painterIcon = painterResource(id = R.drawable.ic_info_black_24dp),
                action = {
                    if (state.parcelDetail != null) {
                        barCodeShown = true
                    }
                }),

            ActionItem(
                stringResource(id = R.string.refresh), icon = Icons.Filled.Refresh, action = onRefresh
            )
        )
    )

    val activity = (LocalContext.current as Activity)
    var orientation by remember {
        mutableStateOf(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
    }
    if (barCodeShown) {
        // This is where you lock to your preferred one

        orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        AlertDialog(
            properties = DialogProperties(usePlatformDefaultWidth = false),
            shape = MaterialTheme.shapes.medium,
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onCloseRequest.
                barCodeShown = false
            },
            title = { Text(text = "Extra info") },
            text = {
                Column {
                    if (state.barcode != null) {
                        Image(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            bitmap = state.barcode.asImageBitmap(),
                            contentDescription = "Barcode"
                        )
                    }

                    Row(horizontalArrangement = Arrangement.Center) {
                        IconButton(onClick = copyAction) {
                            Icon(
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .size(24.dp),
                                painter = painterResource(id = R.drawable.ic_copy),
                                contentDescription = stringResource(
                                    id = R.string.copy
                                ),
                            )
                        }
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(4.dp),
                            textAlign = TextAlign.Center,
                            text = state.trackingCode,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                        )
                    }

                }
            },
            confirmButton = {
                Button(onClick = {
                    barCodeShown = false
                }) {
                    Text(stringResource(id = android.R.string.ok))
                }
            })
    } else {
        orientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
    activity.requestedOrientation = orientation
}
