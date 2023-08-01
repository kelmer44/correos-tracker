package net.kelmer.correostracker.detail.compose

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.kelmer.correostracker.dataApi.model.remote.CorreosApiEvent
import net.kelmer.correostracker.detail.compose.preview.PreviewData
import net.kelmer.correostracker.details.R
import net.kelmer.correostracker.ui.compose.AlphaText
import net.kelmer.correostracker.ui.compose.FaseIcon


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
                AlphaText(alpha = 0.5f) {
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
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Text(
                    text = (event.desTextoResumen ?: ""),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 4.dp, top = 0.dp)
            ) {
                AlphaText(alpha = 0.5f) {
                    Text(
                        text = event.desTextoAmpliado ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Italic
                    )
                }
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
@Preview(uiMode = UI_MODE_NIGHT_YES)
fun EventPreview() {
    Event(
        event = PreviewData.eventList.first(), isFirst = true, isLast = false
    )
}
