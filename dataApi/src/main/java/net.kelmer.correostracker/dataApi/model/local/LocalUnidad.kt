package net.kelmer.correostracker.dataApi.model.local

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * Cached operative unit, keyed by its `codired`. Correos unit master data changes rarely, so a hit
 * here saves a network call per tracking event.
 *
 * [officeType]/[cityName] predate the richer lookup and are kept so previously cached rows keep
 * resolving a [name]; new rows populate [unitName] directly.
 */
@Entity
data class LocalUnidad(
    @PrimaryKey(autoGenerate = false)
    var officeId: String,
    val officeType: String?,
    val cityName: String?,
    val unitName: String? = null,
    val address: String? = null,
    val provinceName: String? = null,
    val postalCode: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
) {
    /**
     * Display name of the unit, e.g. "CTA SANTIAGO DE COMPOSTELA".
     */
    @Ignore
    val name: String? = when {
        !unitName.isNullOrBlank() -> unitName
        !officeType.isNullOrBlank() && !cityName.isNullOrBlank() -> "$officeType $cityName"
        !officeType.isNullOrBlank() -> officeType
        else -> null
    }

    /**
     * Street-level line for the unit, e.g.
     * "CTRA. SANTIAGO AL AEROPUERTO, KM.11 - LAVACOLLA, 15820 SANTIAGO DE COMPOSTELA".
     * Null when the unit has no usable address (some sorting centres return none).
     */
    @Ignore
    val addressLine: String? = listOfNotNull(
        address?.takeIf { it.isNotBlank() },
        listOfNotNull(
            postalCode?.takeIf { it.isNotBlank() },
            cityName?.takeIf { it.isNotBlank() }
        ).joinToString(" ").takeIf { it.isNotBlank() }
    ).joinToString(", ").takeIf { it.isNotBlank() }
}
