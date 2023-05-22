package event

import java.time.LocalDateTime

data class Event(
    val name: String,
    val owner: String,
    val address: String,
    val type: EventType,
    val isBooze: Boolean,
    val startDT: LocalDateTime,
    val endDT: LocalDateTime
)