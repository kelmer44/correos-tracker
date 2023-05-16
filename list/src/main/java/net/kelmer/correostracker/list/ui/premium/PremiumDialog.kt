package net.kelmer.correostracker.list.ui.premium

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import net.kelmer.correostracker.list.R
import net.kelmer.correostracker.ui.compose.CorreosDialog

@Composable
fun PremiumDialog(
    price: String,
    modifier: Modifier = Modifier,
    onBuyClick: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    CorreosDialog(
        modifier = modifier,
        title = stringResource(id = R.string.premium_title),
        onDismiss = onDismiss,
        onConfirm = {
            onBuyClick()
            onDismiss()
        },
        okText = stringResource(id = R.string.premium_cta)
    ) {
        Column {
            Text(
                text = stringResource(id = R.string.premium_desc, price),
                textAlign = TextAlign.Justify
            )

        }
    }
}

@Composable
@Preview
fun PremiumDialogPreview(){
    PremiumDialog(price = "1.99 euros")
}
