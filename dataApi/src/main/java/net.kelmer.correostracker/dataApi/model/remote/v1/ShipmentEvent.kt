package net.kelmer.correostracker.dataApi.model.remote.v1

data class ShipmentEvent(
    val eventDate: String,
    val eventTime: String,
    val phase: String?,
    val summaryText: String?,
    val extendedText: String?,
    val codired: String?
)
