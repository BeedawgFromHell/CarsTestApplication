package kg.rkd.carstestapplication.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ConfirmButton(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onClick: () -> Unit = {}
) {
    Button(modifier = modifier, onClick = onClick, enabled = enabled) {
        Text(text = "Save")
    }
}