package kg.rkd.carstestapplication.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kg.rkd.carstestapplication.R
import kg.rkd.carstestapplication.domain.CarModel
import kg.rkd.carstestapplication.ui_components.DefaultAppBar

@Composable
fun HomeTopBarComponent(
    onSortParamClick: (String) -> Unit = {},
    onSettingsClicked: () -> Unit = {}
) {
    DefaultAppBar(backEnabled = false) {
        var menuIsShowing by remember {
            mutableStateOf(false)
        }
        IconButton(onClick = { menuIsShowing = !menuIsShowing }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_sort),
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
}
