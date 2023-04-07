package net.kelmer.correostracker.util.adapter

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Types
import net.kelmer.correostracker.dataApi.model.remote.CorreosApiEvent
import net.kelmer.correostracker.dataApi.model.remote.CorreosApiParcel
import net.kelmer.correostracker.dataApi.model.remote.Error

class CorreosApiParcelAdapter(
    private val eventAdapter: JsonAdapter<CorreosApiEvent>,
    private val errorAdapter: JsonAdapter<Error>
) : JsonAdapter<List<CorreosApiParcel>>() {
    override fun fromJson(reader: JsonReader): List<CorreosApiParcel> = with(reader) {
        val events = mutableListOf<CorreosApiEvent>()
        val parcel = CorreosApiParcel.allNull()
        beginArray()
        beginObject()
        val validKeys = arrayOf(
            "codEnvio", "refCliente",
            "codProducto",
            "fecha_calculada",
            "largo",
            "ancho",
            "alto",
            "peso"
        )
        while (hasNext()) {

            val nextName = nextName()
            if (reader.peek() == JsonReader.Token.NULL) {
                skipValue()
                continue
            }
            if (validKeys.contains(nextName)) {
                val value = reader.nextString()
                when (nextName) {
                    "codEnvio" -> parcel.codEnvio = value
                    "refCliente" -> parcel.refCliente = value
                    "codProducto" -> parcel.codProducto = value
                    "fecha_calculada" -> parcel.fechaCalculada = value
                    "largo" -> parcel.largo = value
                    "ancho" -> parcel.ancho = value
                    "alto" -> parcel.alto = value
                    "peso" -> parcel.peso = value
                }
                continue
            }
            if (nextName == "error") {
                val error = errorAdapter.fromJson(reader)
                if (error != null) {
                    parcel.error = error
                }
                continue
            }
            if (nextName == "eventos") {

                if (peek() == JsonReader.Token.BEGIN_OBJECT) {
                    val fromJson = eventAdapter.fromJson(reader)
                    if (fromJson != null) {
                        events += fromJson
                    }
                    parcel.eventos = events
                    continue
                }
                beginArray()
                while (hasNext()) {
                    val fromJson = eventAdapter.fromJson(reader)
                    if (fromJson != null) {
                        events += fromJson
                    }
                }
                endArray()
                parcel.eventos = events
                continue
            }
            skipValue()
        }

        endObject()
        endArray()
        return listOf(parcel)
    }

    override fun toJson(writer: JsonWriter, value: List<CorreosApiParcel>?) {
    }

    companion object {
        val FACTORY: JsonAdapter.Factory = Factory { type, _, moshi ->

            val rawType = Types.getRawType(type)
            if (rawType != CorreosApiParcel::class.java && rawType != List::class.java)
                return@Factory null

            val eventAdapter = moshi.adapter<CorreosApiEvent>(CorreosApiEvent::class.java)
            val errorAdapter = moshi.adapter<Error>(Error::class.java)
            return@Factory CorreosApiParcelAdapter(eventAdapter, errorAdapter)
        }
    }
}
