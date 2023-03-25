package kg.rkd.carstestapplication.ui.add_car.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import kg.rkd.carstestapplication.ui.add_car.AddCarScreenState


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun FieldsComponent(modifier: Modifier = Modifier, state: AddCarScreenState) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        val keyboard = LocalSoftwareKeyboardController.current
        OutlinedTextField(
            label = { Text(text = "Name") },
            value = state.name.value,
            keyboardActions = KeyboardActions(
                onDone = { keyboard?.hide() }
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            onValueChange = {
                state.name.value = it
            },
            isError = !state.validateName()
        )

        OutlinedTextField(
            label = { Text(text = "Engine capacity") },
            value = state.engineCapacity.value,
            keyboardActions = KeyboardActions(
                onDone = { keyboard?.hide() }
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            onValueChange = {
                state.engineCapacity.value = it
            },
            isError = !state.validateEngineCapacity()
        )

        OutlinedTextField(
            label = { Text(text = "Year") },
            value = state.year.value,
            keyboardActions = KeyboardActions(
                onDone = { keyboard?.hide() }
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            onValueChange = {
                state.year.value = it
            },
            isError = !state.validateYear()
        )
    }
}

