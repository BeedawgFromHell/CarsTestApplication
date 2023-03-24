package kg.rkd.carstestapplication.ui.home.components

import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kg.rkd.carstestapplication.domain.CarModel
import kg.rkd.carstestapplication.utils.DateTimeUtils

@Composable
fun CarComponent(modifier: Modifier = Modifier, car: CarModel) {
    val screen = LocalConfiguration.current
    val isPortrait = screen.orientation == ORIENTATION_PORTRAIT

    Card(
        modifier = modifier
            .widthIn(max = (screen.screenWidthDp / 2 - 20).dp)
            .heightIn(max = (screen.screenHeightDp / if (isPortrait) 4 else 1).dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 0.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp, vertical = 3.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier,
                    text = car.name,
                    style = if (isPortrait) MaterialTheme.typography.titleSmall
                        else MaterialTheme.typography.titleLarge,

                )
                Text(
                    modifier = Modifier.padding(start = 3.dp),
                    text = DateTimeUtils.millisToFormat(car.createdDate),
                    style = MaterialTheme.typography.labelSmall,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }

            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp),
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