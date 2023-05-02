package net.kelmer.correostracker.create.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import net.kelmer.correostracker.create.R
import net.kelmer.correostracker.dataApi.model.local.LocalParcelReference
import net.kelmer.correostracker.ui.compose.TextRadioButton

@Composable fun StanceInput(
    stance: LocalParcelReference.Stance,
    onStanceChange: (LocalParcelReference.Stance) -> Unit,
) {

    val radioOptions = listOf(
        LocalParcelReference.Stance.INCOMING to stringResource(id = R.string.incoming),
        LocalParcelReference.Stance.OUTGOING to stringResource(id = R.string.outgoing)
    )

    Text(
        text = stringResource(id = R.string.stance).uppercase(),
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )

    Row(modifier = Modifier.fillMaxWidth().selectableGroup(), horizontalArrangement = Arrangement.SpaceEvenly) {
        radioOptions.forEach { (option, text) ->
            TextRadioButton(
                item = option,
                selected = (option == stance),
                onOptionSelected = { onStanceChange(option)},
                text = text
            )
        }
    }

//    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
//        radioOptions.forEach { (thisStance, text) ->
//            Row(
//                Modifier.selectable(
//                    selected = thisStance == stance,
//                    onClick = { onStanceChange(thisStance) },
//                )
//            ) {
//                RadioButton(modifier = Modifier.align(Alignment.CenterVertically),
//                    selected = thisStance == stance,
//                    onClick = { onStanceChange(thisStance) })
//                Text(
//                    text = text,
//                    style = MaterialTheme.typography.bodyMedium.merge(),
//                    modifier = Modifier
//                        .padding(start = 8.dp)
//                        .align(Alignment.CenterVertically)
//                )
//            }
//
//        }
//    }
}
