package net.kelmer.correostracker.data.model.local

import androidx.room.TypeConverter

class StanceConverter {
    @TypeConverter
    fun toStatus(stance: Boolean): LocalParcelReference.Stance {
        return if (stance) {
            LocalParcelReference.Stance.INCOMING
        } else
            LocalParcelReference.Stance.OUTGOING
    }

    @TypeConverter
    fun toBoolean(status: LocalParcelReference.Stance): Boolean = status == LocalParcelReference.Stance.INCOMING
}
