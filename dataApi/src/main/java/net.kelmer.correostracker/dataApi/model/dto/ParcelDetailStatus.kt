package net.kelmer.correostracker.dataApi.model.dto

import androidx.annotation.DrawableRes
import net.kelmer.correostracker.dataApi.R

data class ParcelDetailStatus(
    val fecEvento: String,
    val horEvento: String,
    val fase: String,
    val desTextoResumen: String,
    val desTextoAmpliado: String
)
