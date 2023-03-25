package kg.rkd.carstestapplication.ui.details

import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kg.rkd.carstestapplication.domain.CarModel
import kg.rkd.carstestapplication.ui.HomeFragment
import kg.rkd.carstestapplication.ui_components.DefaultAppBar
import kg.rkd.carstestapplication.utils.DateTimeUtils

class DetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val car = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(
                HomeFragment.CAR_MODEL_ARG_KEY,
                CarModel::class.java
            ) as CarModel
        } else {
            requireArguments().getParcelable<CarModel>(HomeFragment.CAR_MODEL_ARG_KEY) as CarModel
        }

        return ComposeView(requireContext()).apply {
            setContent {
                DetailsScreen(car = car, onBackPressed = {
                    findNavController().popBackStack()
                })
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailsScreen(car: CarModel, onBackPressed: () -> Unit = {}) {
    Scaffold(
        topBar = {
            DefaultAppBar(
                onBackPressed = onBackPressed
            )
        }
    ) {
        val screenWidth = LocalConfiguration.current.screenWidthDp
        val screenHeight = LocalConfiguration.current.screenHeightDp
        val isPortrait =
            LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
                .padding(6.dp)
                .verticalScroll(state = rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (isPortrait) {
                ImageWithLabelComponent(
                    modifier = Modifier
                        .heightIn(max = (screenHeight - screenHeight / 4).dp)
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    image = car.photo,
                    label = car.name,
                    dateTime = DateTimeUtils.millisToFormat(car.createdDate)
                )

                FieldsComponent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp, horizontal = 12.dp),
                    car = car,
                    textStyle = MaterialTheme.typography.titleMedium
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ImageWithLabelComponent(
                        modifier = Modifier
                            .widthIn(max = (screenWidth / 2).dp)
                            .padding(horizontal = 6.dp),
                        image = car.photo,
                        label = car.name,
                        dateTime = DateTimeUtils.millisToFormat(car.createdDate)
                    )

                    FieldsComponent(car = car)
                }
            }
        }
    }
}

@Composable
private fun ImageWithLabelComponent(
    modifier: Modifier = Modifier,
    image: ByteArray,
    label: String = "",
    dateTime: String = "",
) {
    Box(modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant)) {
        Column(
            modifier = Modifier
                .padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier,
                    text = label,
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(text = dateTime)
            }
            Image(
                modifier = Modifier.padding(top = 6.dp),
                bitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
                    .asImageBitmap(), contentDescription = label
            )
        }
    }
}

@Composable
private fun FieldsComponent(
    modifier: Modifier = Modifier,
    car: CarModel,
    textStyle: TextStyle = MaterialTheme.typography.titleLarge
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Year: ", style = textStyle)
            Text(text = car.year.toString(), style = textStyle)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 9.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Engine capacity: ", style = textStyle)
            Text(text = car.engineCapacity.toString(), style = textStyle)
        }
    }
}