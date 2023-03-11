package net.kelmer.correostracker.data.model.remote.v1

import com.squareup.moshi.Json

data class Shipment(
    val shipmentCode: String?,
    @Json(name = "date_delivery_sum")
    val dateDeliverySum: String?,
    val error: Error?,
    val events: List<ShipmentEvent>
)

data class ShipmentEvent(
    val eventDate: String,
    val eventTime: String,
    val phase: String?,
    val summaryText: String?,
    val extendedText: String
)
