package event

import com.google.firebase.Timestamp


data class Event(
    val name: String? = null,
    val owner: String? = null,
    // val address: String? = null,
    //wygoniejsze niz event type skoro sa tylko 2 mozliwosci
    val isOutdoor: Boolean? = null,
    @field:JvmField
    val isBooze: Boolean? = null,
    @field:JvmField
    val isPublic: Boolean? = null,
//    // x coordinate
    val longitude: Double? = null,
//    // y coordinate
    val latitude: Double? = null,
    val creation: Timestamp? = null,
    val start: Timestamp? = null,
    val end: Timestamp? = null,
)