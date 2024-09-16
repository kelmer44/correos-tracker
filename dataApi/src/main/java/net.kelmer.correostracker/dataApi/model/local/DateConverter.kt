package net.kelmer.correostracker.dataApi.model.local

import androidx.room.TypeConverter
import java.util.Date

/**
 * Created by gabriel on 25/03/2018.
 */
class DateConverter {

    @TypeConverter
    fun toDate(timestamp: Long): Date {
        return Date(timestamp)
    }

    @TypeConverter
    fun toTimestamp(date: Date): Long {
        return date.time
    }
}
