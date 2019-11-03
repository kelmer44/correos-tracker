package net.kelmer.correostracker.mock_server

import com.google.gson.annotations.SerializedName
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing

fun Application.main() {

    install(ContentNegotiation) {
        gson()
    }

    routing {
        get("/eventos_envio_servicio/{idEnvio}") {
            val idEnvio = call.parameters["idEnvio"] ?: ""
            print(idEnvio)
            val parcel = CorreosApiParcel(
                idEnvio,
                "ES2019COM0000708025",
                "QD",
                "31/10/2019 00:00",
                "16",
                "23",
                "830",
                "830",
                listOf(
                    CorreosApiEvent(
                        "31/10/2019 00:00",
                        "I010000V",
                        "12:31:43",
                        "4",
                        "Entregado",
                        "Entregado",
                        "UD ARGANDA DEL REY"
                    )
                ),
                Error(
                    "0",
                    ""
                )
            )
            call.respond(HttpStatusCode.OK, parcel)
        }
    }
}

data class CorreosApiParcel(val codEnvio: String,
                            val refCliente: String,
                            val codProducto: String,
                            @SerializedName("fec_calculada")
                            val fechaCalculada: String,
                            val largo: String,
                            val ancho: String,
                            val alto: String,
                            val peso: String,
                            val eventos: List<CorreosApiEvent>,
                            val error: Error?)

data class CorreosApiEvent(val fecEvento: String,
                           val codEvento: String,
                           val horEvento: String,
                           val fase: String? = "?",
                           val desTextoResumen: String,
                           val desTextoAmpliado: String,
                           val unidad: String? = "?")

data class Error(val codError: String,
                 val desError: String)