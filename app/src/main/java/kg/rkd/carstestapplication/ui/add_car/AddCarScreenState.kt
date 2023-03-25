package kg.rkd.carstestapplication.ui.add_car

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import kg.rkd.carstestapplication.domain.CarModel

data class AddCarScreenState(
    val picture: Uri,
    val name: MutableState<String>,
    val year: MutableState<String>,
    val engineCapacity: MutableState<String>,
) {
    private var photoArray: ByteArray? = null

    fun getPictureAsByteArray(context: Context): ByteArray {
        if (photoArray != null) return photoArray!!

        val stream = context.contentResolver.openInputStream(picture)
            ?: throw RuntimeException("Picture not provided")
        val bytes = stream.readBytes()
        stream.close()
        photoArray = bytes
        return bytes
    }

    fun validateName() = name.value.isNotBlank()
    fun validateYear() = year.value.toIntOrNull() != null
    fun validateEngineCapacity() = engineCapacity.value.toFloatOrNull() != null

    fun validate() = validateName() && validateYear() && validateEngineCapacity()

    fun toModel(
        context: Context
    ): CarModel {
        return CarModel(
            name = this.name.value,
            photo = getPictureAsByteArray(context),
            year = year.value.toInt(),
            engineCapacity = engineCapacity.value.toFloat(),
            createdDate = System.currentTimeMillis()
        )
    }

    companion object {
        @Composable
        fun createInstance(
            picture: Uri = Uri.parse(""),
            name: MutableState<String> = rememberSaveable { mutableStateOf("") },
            year: MutableState<String> = rememberSaveable { mutableStateOf("") },
            engineCapacity: MutableState<String> = rememberSaveable { mutableStateOf("") },
        ): AddCarScreenState {
            return AddCarScreenState(
                picture = picture,
                name = name,
                year = year,
                engineCapacity = engineCapacity
            )
        }
    }

}
