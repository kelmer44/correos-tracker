package net.kelmer.correostracker.data.model.remote

import com.squareup.moshi.Json

data class CorreosApiParcel(
    var codEnvio: String? = null,
    var refCliente: String? = null,
    var codProducto: String? = "",
    @Json(name = "fec_calculada")
    var fechaCalculada: String?,
    var largo: String? = null,
    var ancho: String? = null,
    var alto: String? = null,
    var peso: String? = null,
    var eventos: List<CorreosApiEvent>? = emptyList(),
    var error: Error?
) {

    companion object {

        fun allNull() = CorreosApiParcel(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )
    }
}
