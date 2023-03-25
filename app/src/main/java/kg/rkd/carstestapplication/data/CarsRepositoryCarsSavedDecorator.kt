package kg.rkd.carstestapplication.data

import androidx.datastore.preferences.core.stringPreferencesKey
import kg.rkd.carstestapplication.utils.AppDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


interface CarsRepositoryCarsSavedDecorator : CarsRepository {
    fun getCarsSavedByUserCount(): Int
    fun getCarsSavedByUserCountAsFlow(): Flow<Int>
    suspend fun resetCount()
    suspend fun addToCount(value: Int = 1)
}

class CarsRepositoryCarsSavedDecoratorImpl(
    private val carsRepository: CarsRepository,
    private val dataStore: AppDataStore
) : CarsRepositoryCarsSavedDecorator, CarsRepository by carsRepository {

    override fun getCarsSavedByUserCount(): Int =
        dataStore.get(stringPreferencesKey(AppDataStore.SAVED_CARS_COUNT_KEY))?.toIntOrNull() ?: 0

    override fun getCarsSavedByUserCountAsFlow(): Flow<Int> {
        return dataStore.getAsFlow(stringPreferencesKey(AppDataStore.SAVED_CARS_COUNT_KEY))
            .map { it?.toIntOrNull() ?: 0 }
    }

    override suspend fun resetCount() {
        dataStore.set(stringPreferencesKey(AppDataStore.SAVED_CARS_COUNT_KEY), "0")
    }

    override suspend fun addToCount(value: Int) {
        val current =
            dataStore.get(stringPreferencesKey(AppDataStore.SAVED_CARS_COUNT_KEY))?.toIntOrNull()
                ?: 0
        dataStore.set(
            stringPreferencesKey(AppDataStore.SAVED_CARS_COUNT_KEY),
            (current + 1).toString()
        )
    }
}