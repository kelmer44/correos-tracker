package net.kelmer.correostracker.data.model.dto

import net.kelmer.correostracker.data.model.remote.CorreosApiEvent


data class ParcelDetailDTO(val name: String,
                           val code: String,
                           val ancho: String,
                           val alto: String,
                           val largo: String,
                           val peso: String,
                           var refCliente: String,
                           val codProducto: String,
                           val fechaCalculada: String,
                           val states: List<CorreosApiEvent>)
//                           val states: List<ParcelDetailStatus>)