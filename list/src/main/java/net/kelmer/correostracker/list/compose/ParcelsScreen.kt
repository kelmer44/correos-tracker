package net.kelmer.correostracker.list.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava2.subscribeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import net.kelmer.correostracker.dataApi.model.local.LocalParcelReference
import net.kelmer.correostracker.dataApi.model.remote.CorreosApiEvent
import net.kelmer.correostracker.list.ParcelListViewModel
import net.kelmer.correostracker.list.R
import net.kelmer.correostracker.list.compose.feature.FeatureDialog
import net.kelmer.correostracker.list.compose.theme.ThemeDialog
import net.kelmer.correostracker.theme.R.*
import net.kelmer.correostracker.ui.compose.ActionItem
import net.kelmer.correostracker.ui.compose.CircledIcon
import net.kelmer.correostracker.ui.compose.ConfirmDialog
import net.kelmer.correostracker.ui.compose.ErrorView
import net.kelmer.correostracker.ui.compose.FaseIcon
import net.kelmer.correostracker.ui.compose.OverflowMenuAction
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParcelsScreen(
    useDarkTheme: Boolean,
    modifier: Modifier = Modifier,
    viewModel: ParcelListViewModel = viewModel(),
    onAddParcel: () -> Unit = {},
    onParcelClicked: (String) -> Unit = {},
    onLongPressParcel: (String) -> Unit = {},
    onWebClicked: () -> Unit
) {
    val viewState by viewModel.stateOnceAndStream.subscribeAsState(ParcelListViewModel.State())

    var showAbout by remember { mutableStateOf(false) }
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
            Column(modifier = modifier.padding(contentPadding)) {
                val state = viewState

                if (state.list != null) {
                    if (state.list.isNotEmpty()) {
                        ParcelList(
                            state.list,
                            onParcelClicked,
                            viewModel::deleteParcel,
                            viewModel::toggleNotifications,
                            onLongPressParcel
                        )
                    } else {
                        EmptyState(onAddParcel)
                    }
                }
                if (state.loading) {

                }
                if (state.error != null) {
                    val unrecognized = stringResource(id = R.string.error_unrecognized)
                    ErrorView(message = state.error.message ?: unrecognized)
                }
            }
            if (showAbout) {
                FeatureDialog(
                    onWebClick = onWebClicked,
                    onDismiss = { showAbout = false }
                )
            }
            if (showThemeDialog) {
                ThemeDialog(
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
    onLongPressParcel: (String) -> Unit = {}
) {
    LazyColumn {
        items(items = list, key = { it.code }) { parcel ->
            ParcelListItem(
                parcel = parcel,
                onParcelClicked = onParcelClicked,
                onRemoveParcel = onRemoveParcel,
                onToggleNotifications = onToggleNotifications,
                onLongPressParcel = onLongPressParcel
            )
        }
    }
}

@Composable
fun EmptyState(onAddParcel: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            CircledIcon(
                bgColor = MaterialTheme.colorScheme.secondary,
                icon = R.drawable.ic_package,
                contentDescription = "",
                modifier = Modifier
                    .align(CenterHorizontally)
                    .clickable {
                        onAddParcel()
                    }
            )
            Text(
                text = stringResource(id = R.string.emptystate_parcel_list),
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ParcelListItem(
    parcel: LocalParcelReference,
    modifier: Modifier = Modifier,
    onParcelClicked: (String) -> Unit,
    onRemoveParcel: (LocalParcelReference) -> Unit,
    onToggleNotifications: (String, Boolean) -> Unit,
    onLongPressParcel: (String) -> Unit = {}
) {
    ElevatedCard(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .combinedClickable(
                interactionSource = remember {
                    MutableInteractionSource()
                },
                indication = rememberRipple(bounded = true),
                onClick = { onParcelClicked(parcel.trackingCode) },
                onLongClick = { onLongPressParcel(parcel.trackingCode) },
            ),
        shape = RoundedCornerShape(4.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            var openDialog by remember { mutableStateOf(false) }
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
                            if (parcel.notify) {
                                stringResource(id = R.string.menu_disable_notifications)
                            } else {
                                stringResource(id = R.string.menu_enable_notifications)
                            },
                            action = {
                                onToggleNotifications(parcel.trackingCode, !parcel.notify)
                            }
                        ),
                        ActionItem(stringResource(id = R.string.delete), action = {
                            openDialog = true
                        })
                    )
                )
                if (openDialog) {
                    ConfirmDialog(
                        title = stringResource(id = R.string.delete_confirm_title),
                        text = stringResource(id = R.string.delete_confirm_desc),
                        onDismiss = { openDialog = false },
                        onConfirm = { onRemoveParcel(parcel) }
                    )
                }

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
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
            Row {
                Column(
                    Modifier
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .width(32.dp)
                        .height(32.dp)
                ) {
                    ListStateIcon(parcel)
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
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Italic,
                    )
                }
            }
        }
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
                bgColor = colorResource(id = color.stage_error),
                icon = drawable.ic_error,
                contentDescription = ""
            )
        }
        LocalParcelReference.UpdateStatus.UNKNOWN -> {
            CircledIcon(
                bgColor = colorResource(id = color.stage_unknown),
                icon = drawable.ic_questionmark,
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
fun emptyState() {
    EmptyState()
}

@Composable
@Preview
fun previewIcon() {
    ListStateIcon(
        LocalParcelReference(
            "22313",
            "123123",
            "bla",
            LocalParcelReference.Stance.INCOMING,
            CorreosApiEvent("", "", "", "1", "", "", ""),
            1, notify = true,
            updateStatus = LocalParcelReference.UpdateStatus.OK
        )
    )
}

@Composable
@Preview
fun previewList() {
    ParcelList(
        listOf(
            LocalParcelReference(
                "22313",
                "123123",
                "bla",
                LocalParcelReference.Stance.INCOMING,
                CorreosApiEvent("", "", "", "1", "", "", ""),
                1, notify = true,
                updateStatus = LocalParcelReference.UpdateStatus.OK
            ),
            LocalParcelReference(
                "1242345324654",
                "1233423423123",
                "Bla bla",
                LocalParcelReference.Stance.OUTGOING,
                CorreosApiEvent("", "", "", "1", "", "", ""),
                1, notify = true,
                updateStatus = LocalParcelReference.UpdateStatus.OK
            )
        )
    )
}

private val dateFormat = SimpleDateFormat("dd/MM/yyy HH:mm:ss")

private fun sampleList() = listOf(
    LocalParcelReference(
        "22313",
        "123123",
        "bla",
        LocalParcelReference.Stance.INCOMING,
        CorreosApiEvent("", "", "", "1", "", "", ""),
        1, notify = true,
        updateStatus = LocalParcelReference.UpdateStatus.OK
    ),
    LocalParcelReference(
        "1242345324654",
        "1233423423123",
        "Bla bla",
        LocalParcelReference.Stance.OUTGOING,
        CorreosApiEvent("", "", "", "1", "", "", ""),
        1, notify = true,
        updateStatus = LocalParcelReference.UpdateStatus.OK
    )
)
