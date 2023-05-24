package event

import com.google.firebase.Timestamp


data class Event(
    val name: String? = null,
    val owner: String? = null,
    val address: String? = null,
    val type: EventType? = null,
    @field:JvmField
    val isBooze: Boolean? = null,
//    // x coordinate
//    val longitude: Double? = null,
//    // y coordinate
//    val latitude: Double? = null,
    val creation: Timestamp? = null,
    val start: Timestamp? = null,
    val end: Timestamp? = null
)