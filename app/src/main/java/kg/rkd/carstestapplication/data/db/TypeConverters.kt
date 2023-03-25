package kg.rkd.carstestapplication.data.db

import android.util.Base64
import androidx.room.TypeConverter

class TypeConverters {
    @TypeConverter
    fun fromBase64String(value: String): ByteArray {
        return Base64.decode(value, Base64.DEFAULT)
    }

    @TypeConverter
    fun toBase64String(value: ByteArray): String {
        return Base64.encodeToString(value, Base64.DEFAULT)
    }
}