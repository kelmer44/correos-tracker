package net.kelmer.correostracker.detail.compose.preview

import net.kelmer.correostracker.dataApi.model.dto.ParcelDetailDTO
import net.kelmer.correostracker.dataApi.model.remote.CorreosApiEvent

object PreviewData {
    val eventList = listOf(
        CorreosApiEvent(
            fecEvento = "19/03/2018",
            codEvento = "P040000V",
            horEvento = "16:32",
            fase = "2",
            desTextoResumen = "Clasificado",
            desTextoAmpliado = "Envío clasificado en Centro Logístico",
            unidad = "CTA SANTIAGO DE COMPOSTELA"
        ), CorreosApiEvent(
            fecEvento = "19/03/2018",
            codEvento = "P040000V",
            horEvento = "16:32",
            fase = "4",
            desTextoResumen = "Entregado",
            desTextoAmpliado = "Envío clasificado en Centro Logístico",
            unidad = "CTA SANTIAGO DE COMPOSTELA"
        )
    )

    val detail = ParcelDetailDTO(
        "Name",
        "1234",
        "45",
        "35",
        "50",
        "1.5",
        "1234",
        "1234",
        "2022/02/01",
        eventList
    )
}
