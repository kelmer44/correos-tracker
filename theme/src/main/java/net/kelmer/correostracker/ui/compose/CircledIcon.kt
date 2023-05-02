package net.kelmer.correostracker.ui.compose

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.kelmer.correostracker.theme.R
import net.kelmer.correostracker.ui.Fase


@Composable
fun FaseIcon(
    faseString: String?,
    modifier: Modifier = Modifier
) {

    val faseRaw = faseString
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
    CircledIcon(bgColor = colorResource(id = color), icon = icon, contentDescription = "", modifier = modifier)
}

@Composable
fun CircledIcon(
    bgColor: Color,
    @DrawableRes icon: Int,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.size(32.dp)) {
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
   FaseIcon(faseString = "2")
}
