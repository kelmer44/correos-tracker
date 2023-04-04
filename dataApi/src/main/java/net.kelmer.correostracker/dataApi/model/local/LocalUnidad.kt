package net.kelmer.correostracker.dataApi.model.local

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class LocalUnidad(
    @PrimaryKey(autoGenerate = false)
    var officeId: String,
    val officeType: String?,
    val cityName: String?,
) {
    @Ignore
    val name: String? = when {
        !officeType.isNullOrBlank() && !cityName.isNullOrBlank() -> "$officeType $cityName"
        !officeType.isNullOrBlank() -> officeType
        else -> null
    }
}
