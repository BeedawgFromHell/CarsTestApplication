package kg.rkd.carstestapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kg.rkd.carstestapplication.R
import kg.rkd.carstestapplication.R.*
import kg.rkd.carstestapplication.domain.CarModel
import kg.rkd.carstestapplication.ui.components.CarComponent
import kg.rkd.carstestapplication.ui_components.DefaultAppBar
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {
    private val viewModel: CarsViewModel by viewModel()

    private val addCarImagePicker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                findNavController().navigate(
                    R.id.addCarFragment, bundleOf(CAR_PIC_ARG_KEY to it.toString())
                )
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                HomeScreen(
                    viewModel.cars.collectAsState()
                ) {
                    if(viewModel.isAllowedToSaveCar()){
                        addCarImagePicker.launch("image/*")
                    }
                }
            }
        }
    }

    companion object {
        const val CAR_PIC_ARG_KEY = "car_pic"
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(cars: State<List<CarModel>>, onFabClicked: () -> Unit = {}) {
    Scaffold(
        topBar = { DefaultAppBar(backEnabled = false) },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(end = 32.dp, bottom = 32.dp),
                onClick = onFabClicked
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "add car"
                )
            }
        }
    ) {

        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
                .padding(horizontal = 9.dp),
            columns = GridCells.Fixed(2),
            content = {
                items(cars.value) { car ->
                    CarComponent(modifier = Modifier.padding(6.dp), car = car)
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

