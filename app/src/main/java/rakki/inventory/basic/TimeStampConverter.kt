package rakki.inventory.basic

import android.arch.persistence.room.TypeConverter
import java.util.*


class TimeStampConverter {

    companion object {


        /*
                    @TypeConverter
                    fun toDate(value: Long?): Date? {
                        return if (value == null) null else Date(value)
                    }

                    @TypeConverter
                    fun toLong(value: Date?): Long? {
                        return value?.time?.toLong()
                    }*/
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


    /*@RequiresApi(Build.VERSION_CODES.O)
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
    }*/

    /*@TypeConverter
    @JvmStatic
    fun toDate(dateLong: Long?): Date? {
        return if (dateLong == null) null else Date(dateLong)
    }

    @TypeConverter
    @JvmStatic
    fun fromDate(date: Date?): Long? {
        return (if (date == null) null else date.time)?.toLong()
    }
*/


}
