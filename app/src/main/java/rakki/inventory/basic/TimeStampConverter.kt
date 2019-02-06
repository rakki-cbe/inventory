package rakki.inventory.basic

import android.arch.persistence.room.TypeConverter
import android.os.Build
import android.support.annotation.RequiresApi
import java.sql.Date
import java.time.Instant


class TimeStampConverter {
    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        @TypeConverter
        @JvmStatic
        fun fromInstant(value: Instant): Long {
            return value.toEpochMilli()
        }

        @RequiresApi(Build.VERSION_CODES.O)
        @TypeConverter
        @JvmStatic
        fun toInstant(value: Long): Instant {
            return Instant.ofEpochMilli(value)
        }

        @TypeConverter
        @JvmStatic
        fun toDate(dateLong: Long?): Date? {
            return if (dateLong == null) null else Date(dateLong)
        }

        @TypeConverter
        @JvmStatic
        fun fromDate(date: Date?): Long? {
            return (if (date == null) null else date.time)?.toLong()
        }
    }
}
