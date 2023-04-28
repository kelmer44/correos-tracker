package net.kelmer.correostracker.detail.compose

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import net.kelmer.correostracker.dataApi.model.dto.ParcelDetailDTO
import net.kelmer.correostracker.dataApi.model.remote.CorreosApiEvent
import net.kelmer.correostracker.detail.DetailViewModel
import net.kelmer.correostracker.details.R
import net.kelmer.correostracker.ui.compose.ActionItem
import net.kelmer.correostracker.ui.compose.NoSearchAppBar
import net.kelmer.correostracker.ui.theme.CorreosTheme

@Composable
fun DetailAppBar(
    useDarkTheme: Boolean = false,
    state: DetailViewModel.State,
    backAction: () -> Unit = {},
    onRefresh: () -> Unit = {},
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
            ActionItem(
                stringResource(id = R.string.parcel_info),
                painterIcon = painterResource(id = R.drawable.ic_barcode),
                action = {
                    if (state.parcelDetail != null) {
                        barCodeShown = true
                    }
                },
                enabled = !state.isLoading && state.error == null && state.barcode != null
            ),

            ActionItem(
                stringResource(id = R.string.refresh), icon = Icons.Filled.Refresh, action = onRefresh
            )
        )
    )

    val activity = (LocalContext.current as? Activity)
    var orientation by remember {
        mutableStateOf(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
    }
    if (barCodeShown && state.barcode != null) {
        // This is where you lock to your preferred one
        orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        BarCodeDialog(state.trackingCode, state.barcode) {
            barCodeShown = false
        }
    } else {
        orientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
    activity?.requestedOrientation = orientation
}

@Composable
@Preview
fun DetailAppBarPreview() {
    CorreosTheme() {
        DetailAppBar(
            state = DetailViewModel.State(
                "1234",
                ParcelDetailDTO(
                    "Name", "1234", "45", "35", "50", "1.5", "1234", "1234", "2022/02/01", listOf(
                        CorreosApiEvent(
                            "21-02-2023",
                            "1",
                            "16:38",
                            "A",
                            "Entregado",
                            "El paquete ha sido entregado al destinatario",
                            "1234"
                        )
                    )
                )
            )
        )

    }
}
