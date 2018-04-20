package net.kelmer.correostracker.data.model.local

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters

/**
 * Created by gabriel on 25/03/2018.
 */
@Entity
data class LocalParcelReference(
        @PrimaryKey(autoGenerate = false)
        var code: String = "",
        var parcelName: String = "",
        @TypeConverters(StanceConverter::class)

        var stance: Stance = Stance.INCOMING) {


    enum class Stance {
        OUTGOING,
        INCOMING
    }
}