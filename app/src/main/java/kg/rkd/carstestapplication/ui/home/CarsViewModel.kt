package kg.rkd.carstestapplication.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kg.rkd.carstestapplication.domain.CarModel
import kg.rkd.carstestapplication.domain.CarsInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CarsViewModel(
    private val interactor: CarsInteractor
): ViewModel() {

    private val _cars = MutableStateFlow(listOf<CarModel>())
    val cars = _cars.asStateFlow()

    init {
        viewModelScope.launch {
            _cars.value = interactor.getCars()
        }
    }
}