package kg.rkd.carstestapplication.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BillingDataStore(private val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "billing_info")

    fun isBought(key: Preferences.Key<String>): Flow<Boolean> {
        return context.dataStore.data.map {
            it[key]?.toBoolean() ?: false
        }
    }

    suspend fun setBought(key: Preferences.Key<String>) {
        context.dataStore.edit {
            it[key] = true.toString()
        }
    }
}