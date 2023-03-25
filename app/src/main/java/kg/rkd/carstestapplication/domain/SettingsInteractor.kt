package kg.rkd.carstestapplication.domain

import kotlinx.coroutines.flow.Flow

interface SettingsInteractor {
    fun isSubscribed(): Flow<Boolean>
    fun getCarsSavedByUserCount(): Flow<Int>
    fun restoreData()
}