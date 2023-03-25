package kg.rkd.carstestapplication.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class AppDataStore(private val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "billing_info")

    fun getAsFlow(key: Preferences.Key<String>): Flow<String?> {
        return context.dataStore.data.map {
            it[key]
        }
    }

    suspend fun set(key: Preferences.Key<String>, value: String) {
        context.dataStore.edit {
            it[key] = value
        }
    }

    fun get(key: Preferences.Key<String>): String? {
        return runBlocking {
            context.dataStore.data.map {
                it[key]
            }.first()
        }
    }

    companion object{
        const val SAVED_CARS_COUNT_KEY = "saved_cars"
    }
}