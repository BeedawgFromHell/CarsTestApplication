package kg.rkd.carstestapplication.ui.add_car

import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kg.rkd.carstestapplication.domain.CarModel
import kg.rkd.carstestapplication.ui.CarsViewModel
import kg.rkd.carstestapplication.ui.HomeFragment
import kg.rkd.carstestapplication.ui.add_car.components.CarPictureComponent
import kg.rkd.carstestapplication.ui.add_car.components.FieldsComponent
import kg.rkd.carstestapplication.ui.components.ConfirmButton
import kg.rkd.carstestapplication.ui_components.DefaultAppBar
import org.koin.androidx.viewmodel.ext.android.getViewModel

class AddCarFragment : Fragment() {

    private val viewModel: CarsViewModel by lazy {
        requireParentFragment().getViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val carPic = Uri.parse(
            requireArguments().getString(HomeFragment.CAR_PIC_ARG_KEY)
                ?: throw RuntimeException("Photo not provided")
        )

        return ComposeView(requireContext()).apply {
            setContent {
                AddCarScreen(
                    carPic = carPic,
                    onSaveClicked = {
                        viewModel.saveCar(it) {
                            findNavController().popBackStack()
                        }
                    }) {
                    findNavController().popBackStack()
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddCarScreen(
    carPic: Uri,
    onSaveClicked: (CarModel) -> Unit = {},
    onBackPressed: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            DefaultAppBar(onBackPressed = onBackPressed)
        }
    ) {
        val context = LocalContext.current
        val isPortrait =
            LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT
        val screenHeight = LocalConfiguration.current.screenHeightDp
        val screenWidth = LocalConfiguration.current.screenWidthDp

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = screenHeight.dp)
                .padding(it)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            val screenState = AddCarScreenState.createInstance(
                picture = carPic
            )


            val pictureModifier =
                if (isPortrait) Modifier
                    .widthIn(max = screenWidth.dp)
                    .heightIn(max = (screenHeight - (screenHeight / 4) * 2).dp)
                else Modifier
                    .widthIn(max = (screenWidth - (screenWidth / 4)).dp)
                    .heightIn(max = (screenHeight * 1.5).dp)
            CarPictureComponent(
                modifier = pictureModifier,
                pic = screenState.getPictureAsByteArray(context)
            )

            FieldsComponent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 9.dp, vertical = 9.dp),
                state = screenState
            )

            ConfirmButton(
                modifier = Modifier
                    .widthIn(min = (screenWidth / 2).dp)
                    .padding(horizontal = 16.dp),
                enabled = screenState.validate(),
                onClick = {
                    onSaveClicked(
                        screenState.toModel(context)
                    )
                }
            )
        }
    }
}

@Composable
@Preview
private fun Preview() {
    AddCarScreen(
        carPic = Uri.parse("asd")
    )
}