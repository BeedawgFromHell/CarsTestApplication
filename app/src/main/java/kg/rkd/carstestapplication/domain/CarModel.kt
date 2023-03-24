package kg.rkd.carstestapplication.domain

class CarModel(
    val id: Long = 0,
    val name: String,
    val photo: ByteArray,
    val year: Int,
    val engineCapacity: Float,
    val createdDate: Long,
)


