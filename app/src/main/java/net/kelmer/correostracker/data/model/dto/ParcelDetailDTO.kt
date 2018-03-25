package net.kelmer.correostracker.data.model.dto

import net.kelmer.correostracker.data.model.remote.CorreosApiEvent


data class ParcelDetailDTO(val name: String,
                           val code: String,
                           val states: List<CorreosApiEvent>)
//                           val states: List<ParcelDetailStatus>)