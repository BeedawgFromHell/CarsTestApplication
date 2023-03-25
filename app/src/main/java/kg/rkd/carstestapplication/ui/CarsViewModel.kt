package kg.rkd.carstestapplication.ui

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kg.rkd.carstestapplication.domain.CarModel
import kg.rkd.carstestapplication.domain.CarsInteractor
import kg.rkd.carstestapplication.domain.CarsInteractorBillingDecorator
import kg.rkd.carstestapplication.ui.add_car.AddCarScreenState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class CarsViewModel(
    private val interactor: CarsInteractorBillingDecorator
) : ViewModel() {

    private val _cars = MutableStateFlow(listOf<CarModel>())
    val cars = _cars.asStateFlow()

    private val _saveCarTries = MutableStateFlow(0)
    val saveCarTries = _saveCarTries.asStateFlow()

    private val _isSubscribed = MutableStateFlow(false)
    val isSubscribed = _isSubscribed.asStateFlow()

    init {
        startCarsFlow()
    }

    private fun startCarsFlow() {
        interactor.getCars().onEach { _cars.value = it }.launchIn(viewModelScope)
    }


    fun isAllowedToSaveCar() = interactor.isAllowedToSaveCar()
    fun isSubscribed() = interactor.isSubscribed()
    fun saveCar(model: CarModel, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            interactor.saveCar(model)
            withContext(Dispatchers.Main) { onSuccess() }
        }
    }

    fun startSubscriptionPurchaseFlow(onSuccess: () -> Unit) {
        viewModelScope.launch {
            interactor.startSubscriptionPurchaseFlow()
            withContext(Dispatchers.Main) { onSuccess() }
        }
    }

    fun loadSettingsData() {

    }
}