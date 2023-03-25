package kg.rkd.carstestapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import kg.rkd.carstestapplication.R
import kg.rkd.carstestapplication.domain.CarModel
import kg.rkd.carstestapplication.ui.components.CarComponent
import kg.rkd.carstestapplication.ui.components.HomeTopBarComponent
import kg.rkd.carstestapplication.ui.subscription_purchase.SubscriptionPurchasePopUpFragment
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


@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class
)
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
            HomeTopBarComponent(onSortParamClick, onSettingsClicked)
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
        val keyboard = LocalSoftwareKeyboardController.current

        val searchFieldValue = rememberSaveable { mutableStateOf("") }
        val showingList = rememberSaveable(cars.value) {
            if (searchFieldValue.value.isBlank()) mutableStateOf(cars.value)
            else mutableStateOf(cars.value.filter { item ->
                item.name.lowercase().contains(searchFieldValue.value)
            })
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
                .padding(horizontal = 9.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxWidth(),
                columns = GridCells.Fixed(2),
                content = {
                    item(span = { GridItemSpan(2) }) {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = { keyboard?.hide() }),
                            label = { Text(text = "Search") },
                            value = searchFieldValue.value,
                            onValueChange = { string ->
                                searchFieldValue.value = string.lowercase()
                                showingList.value =
                                    cars.value.filter { item ->
                                        item.name.lowercase().contains(searchFieldValue.value)
                                    }
                            })
                    }

                    items(showingList.value) { car ->
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

            val screenSize = LocalConfiguration.current
            if (showingList.value.isEmpty()) {
                Icon(
                    modifier = Modifier
                        .sizeIn(
                            maxWidth = (screenSize.screenWidthDp / 2).dp,
                            maxHeight = (screenSize.screenHeightDp / 2).dp
                        )
                        .padding(top = (screenSize.screenHeightDp / 6).dp),
                    painter = painterResource(id = R.drawable.ic_no_data),
                    contentDescription = "empty list",
                    tint = MaterialTheme.colorScheme.tertiary,
                )
                Text(text = "No data", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}


@Composable
@Preview
private fun Preview() {
    HomeScreen(remember {
        mutableStateOf(listOf())
    })
}

