package net.kelmer.correostracker.data.model.remote

import com.squareup.moshi.Json


data class CorreosApiParcel(val codEnvio: String,
                            val refCliente: String? = null,
                            val codProducto: String? = "",
                            @Json(name="fec_calculada")
                            val fechaCalculada: String?,
                            val largo: String? = null,
                            val ancho: String? = null,
                            val alto: String? = null,
                            val peso: String? = null,
                            val eventos: List<CorreosApiEvent>? = emptyList(),
                            val error: Error?) {


}