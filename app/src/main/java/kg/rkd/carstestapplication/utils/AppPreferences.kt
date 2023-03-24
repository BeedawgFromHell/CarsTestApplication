package kg.rkd.carstestapplication.utils

import android.content.Context

class AppPreferences(private val context: Context) {

    private val pref = context.getSharedPreferences("cars_pref", Context.MODE_PRIVATE)


    fun set(key: String, value: String) {
        pref.edit().putString(key, value).apply()
    }

    fun set(key: String, value: Boolean) {
        pref.edit().putBoolean(key, value).apply()
    }

    fun getString(key: String): String? = pref.getString(key, null)

    fun getBoolean(key: String): Boolean = pref.getBoolean(key, false)
}