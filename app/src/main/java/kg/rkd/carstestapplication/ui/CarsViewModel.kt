package kg.rkd.carstestapplication.ui

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

    private var job: Job? = null
    init {
        startCarsFlow()
    }

    private fun startCarsFlow() {
        _cars.value = listOf()
        if(job != null && job!!.isActive) job!!.cancel()
        job = interactor.getCars().onEach { _cars.value = it }.launchIn(viewModelScope)
    }


    fun isAllowedToSaveCar() = interactor.isAllowedToSaveCar()

    fun saveCar(model: CarModel, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            interactor.saveCar(model)
            withContext(Dispatchers.Main) { onSuccess() }
        }
    }

    fun startSubscriptionPurchaseFlow(onSuccess: () -> Unit){
        interactor.startSubscriptionPurchaseFlow()
        startCarsFlow()
        onSuccess()
    }


}