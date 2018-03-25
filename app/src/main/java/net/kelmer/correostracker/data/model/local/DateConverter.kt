package net.kelmer.correostracker.data.model.local

import android.arch.persistence.room.TypeConverter
import java.util.*


/**
 * Created by gabriel on 25/03/2018.
 */
class DateConverter {

    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return if (timestamp == null) null else Date(timestamp)
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long {
        return date?.time ?: -1
    }

}