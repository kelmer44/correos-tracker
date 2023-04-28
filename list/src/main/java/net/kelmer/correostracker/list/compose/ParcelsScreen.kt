package net.kelmer.correostracker.list.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava2.subscribeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import net.kelmer.correostracker.dataApi.model.local.LocalParcelReference
import net.kelmer.correostracker.dataApi.model.remote.CorreosApiEvent
import net.kelmer.correostracker.list.ParcelListViewModel
import net.kelmer.correostracker.list.R
import net.kelmer.correostracker.list.compose.emptystate.EmptyState
import net.kelmer.correostracker.list.compose.feature.FeatureDialog
import net.kelmer.correostracker.list.compose.parcelitem.ParcelListItem
import net.kelmer.correostracker.list.compose.preview.PreviewData
import net.kelmer.correostracker.ui.compose.pullrefresh.PullRefreshIndicator
import net.kelmer.correostracker.ui.compose.pullrefresh.pullRefresh
import net.kelmer.correostracker.ui.compose.pullrefresh.rememberPullRefreshState
import net.kelmer.correostracker.list.compose.theme.ThemeDialog
import net.kelmer.correostracker.ui.compose.CircledIcon
import net.kelmer.correostracker.ui.compose.ErrorView
import net.kelmer.correostracker.ui.compose.FaseIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParcelsScreen(
    useDarkTheme: Boolean,
    modifier: Modifier = Modifier,
    viewModel: ParcelListViewModel = viewModel(),
    onAddParcel: () -> Unit = {},
    onParcelClicked: (String) -> Unit = {},
    onWebClicked: () -> Unit
) {
    val viewState by viewModel.stateOnceAndStream.subscribeAsState(ParcelListViewModel.State())

    var showAbout by remember { mutableStateOf(!viewModel.showFeature()) }
    var showThemeDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            ParcelsAppBar(
                useDarkTheme,
                viewModel::filter,
                viewModel::refresh,
                onThemeClicked = { showThemeDialog = true },
                onAboutClicked = { showAbout = true },
            )
        },
        floatingActionButton = {
            AddParcelFAB(onAddParcel)
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets,
        content = { contentPadding ->
            Column(modifier = modifier
                .padding(contentPadding)
                .fillMaxHeight()) {
                val state = viewState

                val refreshing by remember { mutableStateOf(state.loading) }
                if (state.list != null) {
                    if (state.list.isNotEmpty()) {
                        ParcelList(
                            state.list,
                            onParcelClicked,
                            viewModel::deleteParcel,
                            viewModel::toggleNotifications,
                            viewModel::refresh,
                            refreshing
                        )
                    } else {
                        EmptyState(state.filter, onAddParcel)
                    }
                }
                if (state.error != null) {
                    val unrecognized = stringResource(id = R.string.error_unrecognized)
                    ErrorView(message = state.error.message ?: unrecognized)
                }
            }
            if (showAbout) {
                FeatureDialog(
                    featureList = viewModel.getFeatureList(),
                    onWebClick = onWebClicked,
                    onDismiss = {
                        showAbout = false
                        viewModel.setShownFeature()
                    }
                )
            }
            if (showThemeDialog) {
                ThemeDialog(
                    preSelectedTheme = viewState.theme,
                    onDismiss = { showThemeDialog = false },
                    onSelect = viewModel::setTheme
                )
            }
        }
    )
}

@Composable
fun ParcelList(
    list: List<LocalParcelReference>,
    onParcelClicked: (String) -> Unit = {},
    onRemoveParcel: (LocalParcelReference) -> Unit = {},
    onToggleNotifications: (String, Boolean) -> Unit = { _, _ -> },
    refresh: () -> Unit,
    refreshing: Boolean
) {
    val listState = rememberLazyListState()

    val state = rememberPullRefreshState(refreshing, refresh)

    Box(
        Modifier
            .pullRefresh(state)
            .fillMaxHeight()) {

        LazyColumn(state = listState, modifier = Modifier.fillMaxHeight()) {
            items(items = list, key = { it.code }) { parcel ->
                ParcelListItem(
                    parcel = parcel,
                    onParcelClicked = onParcelClicked,
                    onRemoveParcel = onRemoveParcel,
                    onToggleNotifications = onToggleNotifications
                )
            }
        }
        PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))
    }
}


@Composable
fun ListStateIcon(parcel: LocalParcelReference) {
    when (parcel.updateStatus) {
        LocalParcelReference.UpdateStatus.OK -> {
            FaseIcon(faseString = parcel.ultimoEstado?.fase)
        }

        LocalParcelReference.UpdateStatus.ERROR -> {
            CircledIcon(
                bgColor = colorResource(id = R.color.stage_error),
                icon = R.drawable.ic_error,
                contentDescription = ""
            )
        }

        LocalParcelReference.UpdateStatus.UNKNOWN -> {
            CircledIcon(
                bgColor = colorResource(id = R.color.stage_unknown),
                icon = R.drawable.ic_questionmark,
                contentDescription = ""
            )
        }

        else -> {
            CircularProgressIndicator()
        }
    }
}

@Composable
@Preview
fun previewIcon() {
    ListStateIcon(
        PreviewData.parcelList[0]
    )
}

@Composable
@Preview
fun previewList() {
    ParcelList(
        PreviewData.parcelList,
        refresh = { },
        refreshing = false
    )
}
