package net.kelmer.correostracker.dataApi.model.dto

import net.kelmer.correostracker.dataApi.model.remote.CorreosApiEvent

data class ParcelDetailDTO(
    val name: String,
    val code: String,
    val ancho: String,
    val alto: String,
    val largo: String,
    val peso: String,
    var refCliente: String,
    val codProducto: String,
    val fechaCalculada: String,
    val states: List<CorreosApiEvent>
) {

    fun isDelivered(): Boolean = states.lastOrNull()?.isEntregado() ?: false
    fun containsDimensions(): Boolean = ancho.isNotBlank() && largo.isNotBlank() && alto.isNotBlank()

}
//                           val states: List<ParcelDetailStatus>)
