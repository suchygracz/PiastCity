package event

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude


data class Event(
    val name: String? = null,
    val owner: String? = null,
    val address: String? = null
//    val type: EventType? = null,
//    @field:JvmField
//    val isBooze: Boolean? = null
//    val startDT: Timestamp? = null,
//    val endDT: Timestamp? = null
)