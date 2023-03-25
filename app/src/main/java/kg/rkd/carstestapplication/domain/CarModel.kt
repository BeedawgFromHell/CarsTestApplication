package kg.rkd.carstestapplication.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CarModel(
    val id: Long = 0,
    val name: String,
    val photo: ByteArray,
    val year: Int,
    val engineCapacity: Float,
    val createdDate: Long,
    val shouldBeBlurred: Boolean = false,
) : Parcelable {

    companion object{
        const val BY_YEAR = "year"
        const val BY_ENGINE = "engine"
        const val BY_DATE = "date"
        const val BY_NAME = "name"
        const val BY_DEFAULT = BY_DATE
    }
}




