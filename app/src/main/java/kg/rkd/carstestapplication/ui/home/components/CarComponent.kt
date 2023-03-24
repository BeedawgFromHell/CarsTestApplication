package kg.rkd.carstestapplication.ui.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kg.rkd.carstestapplication.domain.CarModel

@Composable
fun CarComponent(modifier: Modifier = Modifier, car: CarModel) {
    Card(modifier = modifier) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth().padding(6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = car.name)
            }
        }
    }
}

@Composable
@Preview
private fun Preview() {
    Surface(modifier = Modifier.fillMaxSize()) {
        CarComponent(
            car =
            CarModel(
                name = "BMW X6",
                photo = "",
                year = 2021,
                engineCapacity = 5.5f,
                createdDate = 12312391723987
            )
        )
    }
}