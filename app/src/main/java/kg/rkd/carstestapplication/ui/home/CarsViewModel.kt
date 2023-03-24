package kg.rkd.carstestapplication.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kg.rkd.carstestapplication.domain.CarModel
import kg.rkd.carstestapplication.domain.CarsInteractor
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CarsViewModel(
    private val interactor: CarsInteractor
) : ViewModel() {

    private val _cars = MutableStateFlow(listOf<CarModel>())
    val cars = _cars.asStateFlow()

    init {
        interactor.getCars().onEach { _cars.value = it }.launchIn(viewModelScope)
    }
}