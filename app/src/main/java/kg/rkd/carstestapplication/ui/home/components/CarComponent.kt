package kg.rkd.carstestapplication.ui.home.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kg.rkd.carstestapplication.domain.CarModel
import kg.rkd.carstestapplication.utils.DateTimeUtils

@Composable
fun CarComponent(modifier: Modifier = Modifier, car: CarModel) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(vertical = 12.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = car.name, style = MaterialTheme.typography.headlineSmall)
                Text(
                    text = DateTimeUtils.millisToFormat(car.createdDate),
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 260.dp)
                    .padding(top = 6.dp),
                bitmap = BitmapFactory.decodeByteArray(car.photo, 0, car.photo.size)
                    .asImageBitmap(),
                contentDescription = "car_image",
                contentScale = ContentScale.FillWidth
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
private fun Preview() {

    Scaffold(modifier = Modifier.fillMaxSize()) {
        it
        CarComponent(
            car = CarModel(
                name = "BMW X6",
                photo = LocalContext.current.resources.openRawResource(kg.rkd.carstestapplication.R.raw.moskvich)
                    .readBytes(),
                year = 2021,
                engineCapacity = 5.5f,
                createdDate = 12312391723987
            )
        )
    }
}