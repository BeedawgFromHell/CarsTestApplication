package kg.rkd.carstestapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kg.rkd.carstestapplication.domain.CarModel
import kg.rkd.carstestapplication.domain.CarsInteractorBillingDecorator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CarsViewModel(
    private val interactor: CarsInteractorBillingDecorator
) : ViewModel() {

    private val _cars = MutableStateFlow(listOf<CarModel>())
    val cars = _cars.asStateFlow()


    init {
        startCarsFlow()
    }

    private fun startCarsFlow() {
        interactor.getCars().onEach { _cars.value = it }.launchIn(viewModelScope)
    }

    fun isAllowedToSaveCar() = interactor.isAllowedToSaveCar()
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
}