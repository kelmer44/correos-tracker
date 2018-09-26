package net.kelmer.correostracker.data.model.remote

import com.google.gson.annotations.SerializedName


data class CorreosApiParcel(val codEnvio: String,
                            val refCliente: String,
                            val codProducto: String,
                            @SerializedName("fec_calculada")
                            val fechaCalculada: String,
                            val largo: String,
                            val ancho: String,
                            val alto: String,
                            val peso: String,
                            val eventos: List<CorreosApiEvent>,
                            val error: Error?) {


    fun isDelivered(): Boolean = eventos.get(eventos.size - 1).desTextoResumen.equals("Entregado")
}