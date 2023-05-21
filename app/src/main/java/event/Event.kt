package event

import java.util.Date

data class Event(
    val name: String,
    val localization: String,
    val type: EventType,
    val isBooze: Boolean,
    val startDT: Date,
    val endDT: Date
)