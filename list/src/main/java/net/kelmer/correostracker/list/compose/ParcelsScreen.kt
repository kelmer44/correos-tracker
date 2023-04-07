package net.kelmer.correostracker.list.compose

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import net.kelmer.correostracker.dataApi.model.local.LocalParcelReference
import net.kelmer.correostracker.dataApi.model.remote.CorreosApiEvent
import net.kelmer.correostracker.ui.Fase
import net.kelmer.correostracker.list.ParcelListViewModel
import net.kelmer.correostracker.list.R
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
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
    if (list.isNotEmpty()) {
        LazyColumn {
            items(items = list, key = { it.code }) { parcel ->
                ParcelListItem(parcel = parcel) {}
            }
        }
    } else {
        Box(modifier =  Modifier
            .fillMaxHeight()
            .fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.emptystate_parcel_list),
                modifier = Modifier.align(Alignment.Center)
            )
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
                    FaseIcon(parcel)
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


@Composable
fun FaseIcon(parcel: LocalParcelReference) {


    val faseRaw = parcel.ultimoEstado?.fase
    val faseNumber: Int? = if (faseRaw == "?") null else faseRaw?.toIntOrNull()
    val fase = if (faseNumber != null) Fase.fromFase(faseNumber) else Fase.OTHER


    val color = when (fase) {
        Fase.OTHER -> R.color.stage_unknown
        Fase.ERROR -> R.color.stage_error
        Fase.ENTREGADO -> R.color.stage_delivered
        else -> R.color.stage_delivering
    }
    val icon = when (fase) {
        Fase.ERROR -> R.drawable.ic_error
        Fase.PRE -> R.drawable.ic_assignment_turned_in
        Fase.ENCAMINO -> R.drawable.ic_delivering
        Fase.REPARTO -> R.drawable.ic_reparto
        Fase.ENTREGADO -> R.drawable.ic_check_white
        else -> R.drawable.ic_questionmark
    }
    when (parcel.updateStatus) {
        LocalParcelReference.UpdateStatus.OK -> {
            CircledIcon(bgColor = colorResource(id = color), icon = icon, contentDescription = "")
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
fun CircledIcon(
    bgColor: Color,
    @DrawableRes icon: Int,
    contentDescription: String
) {
    Box {
        Canvas(
            modifier = Modifier.size(32.dp),
            onDraw = {
                drawCircle(color = bgColor)
            }
        )
        Image(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(8.dp),
            painter = painterResource(id = icon),
            contentDescription = contentDescription
        )
    }
}

@Composable
@Preview
fun previewIcon() {
    FaseIcon(
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

private
val dateFormat = SimpleDateFormat("dd/MM/yyy HH:mm:ss")
