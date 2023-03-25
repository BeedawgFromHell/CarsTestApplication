package kg.rkd.carstestapplication.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kg.rkd.carstestapplication.domain.SettingsInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val interactor: SettingsInteractor
) : ViewModel() {

    private val _isSubscribed = MutableStateFlow(false)
    val isSubscribed = _isSubscribed.asStateFlow()

    private val _tries = MutableStateFlow(0)
    val tries = _tries.asStateFlow()

    init {
        interactor.isSubscribed().onEach { _isSubscribed.value = it }.launchIn(viewModelScope)
        interactor.getTriesToSaveCar().onEach { _tries.value = it }.launchIn(viewModelScope)
    }

    fun reset() {
        viewModelScope.launch {
            interactor.reset()
        }
    }
}