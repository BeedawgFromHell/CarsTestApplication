package kg.rkd.carstestapplication.ui.add_car.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale

@Composable
fun CarPictureComponent(modifier: Modifier = Modifier, pic: ByteArray) {
    val picture = BitmapFactory.decodeByteArray(pic, 0, pic.size).asImageBitmap()

    Image(
        modifier = modifier,
        bitmap = picture,
        contentDescription = "car picture",
        contentScale = ContentScale.Inside
    )
}
