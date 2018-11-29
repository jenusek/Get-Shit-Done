package pl.cobaltan.getshitdone.Database

import android.arch.persistence.room.TypeConverter
import java.util.*

class DatabaseConverter {
    @TypeConverter
    fun datefromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun calfromTimestamp(value: Long?): Calendar? {
        return if (value == null) null else {
            val cal = Calendar.getInstance()
            cal.time = Date(value)
            cal
        }

    }

    @TypeConverter
    fun calToTimestamp(calendar: Calendar) : Long? {
        return calendar.time.time
    }


}