package kg.rkd.carstestapplication.ui.components

import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kg.rkd.carstestapplication.R
import kg.rkd.carstestapplication.domain.CarModel
import kg.rkd.carstestapplication.utils.DateTimeUtils

@Composable
fun CarComponent(modifier: Modifier = Modifier, car: CarModel) {
    val screen = LocalConfiguration.current
    val isPortrait = screen.orientation == ORIENTATION_PORTRAIT
    val height = (screen.screenHeightDp / if (isPortrait) 4 else 1).dp
    val width = (screen.screenWidthDp / 2 - 20).dp

    Card(
        modifier = modifier
            .widthIn(max = width)
            .heightIn(max = height)
    ) {


        Box {
            val contentSize = remember { mutableStateOf(IntSize(0, 0)) }
            val contentModifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned {
                    contentSize.value = it.size
                }
            Column(
                modifier = if (car.shouldBeBlurred) contentModifier.blur(
                    12.dp, edgeTreatment = BlurredEdgeTreatment(MaterialTheme.shapes.medium)
                ) else contentModifier,
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
                        text = car.name.removeSuffix("blur"),
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
                    contentScale = ContentScale.Fit
                )
            }

            if (car.shouldBeBlurred) {
                val size = contentSize.value
                Icon(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .sizeIn(
                            minHeight = (size.height / 4).dp,
                            minWidth = (size.width / 4).dp,
                        ),
                    painter = painterResource(id = R.drawable.ic_subscription),
                    contentDescription = "subscription"
                )
            }
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
                photo = LocalContext.current.resources.openRawResource(R.raw.moskvich)
                    .readBytes(),
                year = 2021,
                engineCapacity = 5.5f,
                createdDate = 12312391723987,
                shouldBeBlurred = true
            )
        )
    }
}