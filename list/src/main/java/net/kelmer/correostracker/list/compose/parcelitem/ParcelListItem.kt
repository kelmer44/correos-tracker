package net.kelmer.correostracker.list.compose.parcelitem

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.kelmer.correostracker.dataApi.model.local.LocalParcelReference
import net.kelmer.correostracker.list.R
import net.kelmer.correostracker.list.compose.ListStateIcon
import net.kelmer.correostracker.list.compose.preview.PreviewData
import net.kelmer.correostracker.ui.compose.ActionItem
import net.kelmer.correostracker.ui.compose.ConfirmDialog
import net.kelmer.correostracker.ui.compose.OverflowMenuAction
import net.kelmer.correostracker.util.copyToClipboard
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ParcelListItem(
    parcel: LocalParcelReference,
    modifier: Modifier = Modifier,
    onParcelClicked: (String) -> Unit = {},
    onRemoveParcel: (LocalParcelReference) -> Unit = {},
    onToggleNotifications: (String, Boolean) -> Unit = { _, _ -> }
) {

    val dateFormat = SimpleDateFormat("dd/MM/yyy HH:mm:ss")
    val context = LocalContext.current

    ElevatedCard(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .combinedClickable(
                interactionSource = remember {
                    MutableInteractionSource()
                },
                indication = rememberRipple(bounded = true),
                onClick = { onParcelClicked(parcel.trackingCode) },
                onLongClick = {
                    context.copyToClipboard(parcel.trackingCode)
                },
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
                    Text(
                        text = parcel.trackingCode,
                        style = MaterialTheme.typography.labelMedium
                    )
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
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (parcel.updateStatus == LocalParcelReference.UpdateStatus.ERROR)
                            stringResource(id = R.string.status_unknown)
                        else parcel.ultimoEstado?.buildUltimoEstado() ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    with(parcel.lastChecked){
                        if(this != null && this >0) {
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
    }
}

@Composable
@Preview
fun previewList() {
    ParcelListItem(
        PreviewData.parcelList.first(),
    )
}
