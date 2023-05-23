package net.kelmer.correostracker.list.ui.preview

import net.kelmer.correostracker.dataApi.model.local.LocalParcelReference
import net.kelmer.correostracker.dataApi.model.remote.CorreosApiEvent
import net.kelmer.correostracker.list.R
import net.kelmer.correostracker.list.feature.Feature

object PreviewData {

    val featureList = listOf(
        Feature("2.3.0", R.string.changes_2_3_3),
        Feature("2.1.0", R.string.changes_2_1_0),
    )

    val parcelList = listOf(
        LocalParcelReference(
            "22313",
            "123123",
            "bla",
            LocalParcelReference.Stance.INCOMING,
            CorreosApiEvent("", "", "", "1", "Resumen 1", "Texto ampliado 1", "CTA Santiago de Compostela"),
            1, notify = true,
            updateStatus = LocalParcelReference.UpdateStatus.OK,
        ),
        LocalParcelReference(
            "1242345324654",
            "1233423423123",
            "Bla bla",
            LocalParcelReference.Stance.OUTGOING,
            CorreosApiEvent("", "", "", "1", "Resumen 2", "Texto ampliado 1", "CTA Santiago de Compostela"),
            1, notify = true,
            updateStatus = LocalParcelReference.UpdateStatus.OK
        )
    )

}
