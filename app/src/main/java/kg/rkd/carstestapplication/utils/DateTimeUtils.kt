package kg.rkd.carstestapplication.utils

import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {

    fun millisToFormat(millis: Long, pattern: String = "dd.MM.yyyy HH:mm"): String {
        val format = SimpleDateFormat(pattern, Locale.getDefault())
        return format.format(Date(millis))
    }
}