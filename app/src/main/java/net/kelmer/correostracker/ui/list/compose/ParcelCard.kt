package net.kelmer.correostracker.ui.list.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.work.ListenableWorker
import net.kelmer.correostracker.R
import net.kelmer.correostracker.data.Resource
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.ui.list.ParcelListViewModel
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date


@Composable
fun ParcelListScreen(viewModel: ParcelListViewModel) {
    val items by viewModel.parcelList.observeAsState(Resource.inProgress())
    ParcelList(parcelList = items)
}

@Composable
fun ParcelList(parcelList: Resource<List<LocalParcelReference>>) {
    if (parcelList is Resource.Success) {
        LazyColumn {
            itemsIndexed(items = parcelList.data) { index, item ->
                ParcelCard(parcel = item, index = index) {
                    Timber.i("Logging click!")
                }
            }
        }
    }
}

@Composable
fun ParcelCard(parcel: LocalParcelReference, index: Int, onClick: (Int) -> Unit) {

    val dateFormat = SimpleDateFormat("dd/MM/yyy HH:mm:ss")
    val lastCheckedValue = parcel.lastChecked
    val lastChecked = if (lastCheckedValue != null) {
        stringResource(id = R.string.lastchecked, dateFormat.format(Date(lastCheckedValue)))
    } else {
        ""
    }

    Card(
        modifier = Modifier
            .padding(8.dp, 4.dp)
            .fillMaxWidth()
            .clickable { onClick(index) },
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp
    ) {
        Surface() {
            Row(Modifier
                .padding(4.dp)
                .fillMaxSize()
            ) {
                Column() {
                    Text(
                        text = parcel.parcelName,
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = parcel.code)
                    Text(text = parcel.stance.name,
                        style = MaterialTheme.typography.subtitle1,
                        fontStyle = FontStyle.Italic
                    )
                    Divider(color = Color(android.graphics.Color.GRAY), thickness = 1.dp,
                        modifier = Modifier.padding(2.dp))
                    Column() {
                        Text(text = parcel.ultimoEstado?.buildUltimoEstado() ?: "")
                        Text(text = lastChecked)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ParcelCardListPreview() {
    val parcel1 = LocalParcelReference(
        code = "1234",
        trackingCode = "1234",
        parcelName = "My parcel",
        stance = LocalParcelReference.Stance.INCOMING,
        notify = false,
        updateStatus = LocalParcelReference.UpdateStatus.OK
    )
    val parcel2 = LocalParcelReference(
        code = "1234",
        trackingCode = "1234",
        parcelName = "My Second Parcel",
        stance = LocalParcelReference.Stance.INCOMING,
        notify = false,
        updateStatus = LocalParcelReference.UpdateStatus.OK
    )


    ParcelList(Resource.success(listOf(parcel1, parcel2)))
}
