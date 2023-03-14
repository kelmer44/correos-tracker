package net.kelmer.correostracker.data.model.remote.v1

import com.squareup.moshi.Json

data class Shipment(
    val shipmentCode: String?,
    @Json(name = "date_delivery_sum")
    val dateDeliverySum: String?,
    val error: Error?,
    val events: List<ShipmentEvent> = emptyList()
)
