package kg.rkd.carstestapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kg.rkd.carstestapplication.domain.CarModel
import kg.rkd.carstestapplication.domain.CarsInteractor
import kg.rkd.carstestapplication.domain.CarsInteractorBillingDecorator
import kg.rkd.carstestapplication.ui.add_car.AddCarScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CarsViewModel(
    private val interactor: CarsInteractorBillingDecorator
) : ViewModel() {

    private val _cars = MutableStateFlow(listOf<CarModel>())
    val cars = _cars.asStateFlow()

    init {
        interactor.getCars().onEach { _cars.value = it }.launchIn(viewModelScope)
    }

    fun isAllowedToSaveCar() = interactor.isAllowedToSaveCar()

    fun saveCar(model: CarModel, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            interactor.saveCar(model)
            withContext(Dispatchers.Main) { onSuccess() }
        }
    }
}