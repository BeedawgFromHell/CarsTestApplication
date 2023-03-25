package kg.rkd.carstestapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import kg.rkd.carstestapplication.R
import kg.rkd.carstestapplication.R.drawable
import kg.rkd.carstestapplication.domain.CarModel
import kg.rkd.carstestapplication.ui.components.CarComponent
import kg.rkd.carstestapplication.ui.subscription_purchase.SubscriptionPurchasePopUpFragment
import kg.rkd.carstestapplication.ui_components.DefaultAppBar
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {
    companion object {
        const val CAR_PIC_ARG_KEY = "car_pic"
        const val CAR_MODEL_ARG_KEY = "car_model"
    }

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
                    cars = viewModel.cars.collectAsStateWithLifecycle(),
                    onSortParamClick = { viewModel.setSortParam(it) },
                    onSettingsClicked = {
                        findNavController().navigate(R.id.settingsFragment)
                    },
                    onFabClicked = {
                        if (viewModel.isAllowedToSaveCar()) {
                            addCarImagePicker.launch("image/*")
                        } else {
                            showSubscriptionDialog()
                        }
                    },
                    onSubscriptionClicked = ::showSubscriptionDialog,
                    onDetailsClicked = {
                        findNavController().navigate(
                            R.id.detailsFragment, bundleOf(
                                CAR_MODEL_ARG_KEY to it
                            )
                        )
                    }
                )
            }
        }
    }


    private fun showSubscriptionDialog() {
        SubscriptionPurchasePopUpFragment.newInstance().show(childFragmentManager, "subscription")
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun HomeScreen(
    cars: State<List<CarModel>>,
    onSortParamClick: (String) -> Unit = {},
    onSettingsClicked: () -> Unit = {},
    onFabClicked: () -> Unit = {},
    onSubscriptionClicked: () -> Unit = {},
    onDetailsClicked: (CarModel) -> Unit = {},
) {
    Scaffold(
        topBar = {
            DefaultAppBar(backEnabled = false) {
                var menuIsShowing by remember {
                    mutableStateOf(false)
                }
                IconButton(onClick = { menuIsShowing = !menuIsShowing }) {
                    Icon(
                        painter = painterResource(id = drawable.ic_sort),
                        contentDescription = "sorting"
                    )
                }

                DropdownMenu(
                    expanded = menuIsShowing,
                    onDismissRequest = { menuIsShowing = false }) {

                    DropdownMenuItem(
                        text = { Text(text = "by Name") },
                        onClick = { onSortParamClick(CarModel.BY_NAME) })
                    DropdownMenuItem(
                        text = { Text(text = "by Created Time") },
                        onClick = { onSortParamClick(CarModel.BY_DATE) })
                    DropdownMenuItem(
                        text = { Text(text = "by Engine Capacity") },
                        onClick = { onSortParamClick(CarModel.BY_ENGINE) })
                    DropdownMenuItem(
                        text = { Text(text = "by Year") },
                        onClick = { onSortParamClick(CarModel.BY_YEAR) })
                    DropdownMenuItem(
                        text = { Text(text = "by Default") },
                        onClick = { onSortParamClick(CarModel.BY_DEFAULT) })
                }

                IconButton(modifier = Modifier.padding(9.dp), onClick = onSettingsClicked) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "settings"
                    )
                }
            }
        },
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
                    CarComponent(
                        modifier = Modifier
                            .padding(6.dp)
                            .animateItemPlacement()
                            .clickable {
                                if (!car.shouldBeBlurred) onDetailsClicked(car)
                            },
                        car = car,
                        onSubscriptionClicked = onSubscriptionClicked
                    )
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

