package net.kelmer.correostracker.detail.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rxjava2.subscribeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import net.kelmer.correostracker.ads.BannerView
import net.kelmer.correostracker.ads.BuildConfig
import net.kelmer.correostracker.dataApi.model.remote.CorreosApiEvent

import net.kelmer.correostracker.detail.DetailViewModel
import net.kelmer.correostracker.detail.compose.preview.PreviewData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    isPremium: Boolean,
    useDarkTheme: Boolean,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = viewModel(),
    backAction: () -> Unit = {}
) {
    val viewState by viewModel.stateOnceAndStream.subscribeAsState(DetailViewModel.State(""))
    Column {
        Scaffold(
            modifier = modifier.weight(1f),
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
        if (!isPremium) {
            BannerView(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 50.dp),
                isTest = BuildConfig.DEBUG
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
    }
}

@Composable
@Preview
fun EventListSample() {
    EventList(
        PreviewData.eventList
    )
}
