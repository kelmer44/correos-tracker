package net.kelmer.correostracker.data.model.remote.v1

data class Event(
    val eventDate: String?,
    val eventTime: String?,
    val phase: String,
    val colour: String,
    val summaryText: String?,
    val extendedText: String?
)
