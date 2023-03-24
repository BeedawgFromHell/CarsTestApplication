package kg.rkd.carstestapplication.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import kg.rkd.carstestapplication.domain.CarModel
import kg.rkd.carstestapplication.ui.home.components.CarComponent
import kg.rkd.carstestapplication.ui_components.DefaultAppBar

class HomeFragment : Fragment() {

    private val viewModel: CarsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                HomeScreen(
                    viewModel.cars.collectAsState()
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(cars: State<List<CarModel>>) {
    Scaffold(
        topBar = { DefaultAppBar() }
    ) {
        it
        LazyColumn(modifier = Modifier.fillMaxWidth(), content = {
            items(cars.value) { car ->
                CarComponent(modifier = Modifier.padding(vertical = 6.dp), car = car)
            }
        })
    }
}

@Composable
@Preview
private fun Preview() {
    HomeScreen(remember {
        mutableStateOf(listOf())
    })
}

