package kg.rkd.carstestapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kg.rkd.carstestapplication.domain.CarModel
import kg.rkd.carstestapplication.domain.CarsInteractorBillingDecorator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CarsViewModel(
    private val interactor: CarsInteractorBillingDecorator
) : ViewModel() {

    private val _cars = MutableStateFlow(listOf<CarModel>())
    val cars = _cars.asStateFlow()

    private val sortParameter = MutableStateFlow(CarModel.BY_DATE to false)

    init {
        startCarsFlow()
    }

    fun setSortParam(param: String) {
        if(param == sortParameter.value.first) {
            sortParameter.value = param to !sortParameter.value.second
        } else {
            sortParameter.value = param to false
        }
    }

    private fun List<CarModel>.sorted(param: String, descending: Boolean = false): List<CarModel> {
        return if (descending) {
            when (param) {
                CarModel.BY_DATE -> this.sortedByDescending { it.createdDate }
                CarModel.BY_ENGINE -> this.sortedByDescending { it.engineCapacity }
                CarModel.BY_NAME -> this.sortedByDescending { it.name }
                CarModel.BY_YEAR -> this.sortedByDescending { it.year }
                else -> this
            }
        } else {
            when (param) {
                CarModel.BY_DATE -> this.sortedBy { it.createdDate }
                CarModel.BY_ENGINE -> this.sortedBy { it.engineCapacity }
                CarModel.BY_NAME -> this.sortedBy { it.name }
                CarModel.BY_YEAR -> this.sortedBy { it.year }
                else -> this
            }
        }
    }

    private fun startCarsFlow() {
        sortParameter.combine(interactor.getCars()) { sortParam, cars ->
            cars.sorted(sortParam.first, sortParam.second)
        }.onEach { _cars.value = it }.launchIn(viewModelScope)
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