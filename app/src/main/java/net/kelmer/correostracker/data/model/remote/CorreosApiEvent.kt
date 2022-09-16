package net.kelmer.correostracker.data.model.remote

/**
 *
 * "fecEvento": "19/03/2018",
 * "horEvento": "19:00:16",
 * "codEvento": "P040000V",
 * "fase": "2",
 * "color": "V",
 * "desTextoResumen": "Clasificado",
 * "desTextoAmpliado": "Envío clasificado en Centro Logístico",
 * "accionWeb": null,
 * "paramAccionWeb": null,
 * "codired": "1558394",
 * "unidad": "CTA SANTIAGO DE COMPOSTELA
 */
data class CorreosApiEvent(
    val fecEvento: String,
    val codEvento: String,
    val horEvento: String,
    val fase: String? = "?",
    val desTextoResumen: String? = "",
    val desTextoAmpliado: String? = "",
    val unidad: String? = "?"
) {

    fun isEntregado() = desTextoResumen == "Entregado"
    fun buildUltimoEstado() = if (!unidad.isNullOrBlank()) "$desTextoResumen en $unidad" else desTextoResumen
}

