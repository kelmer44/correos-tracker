package net.kelmer.correostracker.dataApi.model.local

import androidx.room.TypeConverter

class UpdateStatusConverter {
    @TypeConverter
    fun toUpdateStatus(status: Int): LocalParcelReference.UpdateStatus {
        LocalParcelReference.UpdateStatus.values().forEach {
            if (status == it.ordinal) {
                return it
            }
        }
        return LocalParcelReference.UpdateStatus.UNKNOWN
    }

    @TypeConverter
    fun toInt(status: LocalParcelReference.UpdateStatus): Int = status.ordinal
}
