package net.kelmer.correostracker.data.model.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import net.kelmer.correostracker.data.model.remote.CorreosApiEvent

/**
 * Created by gabriel on 25/03/2018.
 */
@Entity
data class LocalParcelReference(
    @PrimaryKey(autoGenerate = false)
    /**
     * This is no longer code, code was initially the tracking code,
     * code was initially used as primary key but then was dismissed when people tried
     * to put inadvertedly the same code multiple times, and then complained the app did not
     * support multiple packages, because people are dumb like that.
     */
    var code: String = "",
    var trackingCode: String = "",
    var parcelName: String = "",
    @TypeConverters(StanceConverter::class)
    var stance: Stance = Stance.INCOMING,
    @Embedded
    var ultimoEstado: CorreosApiEvent? = null,
    var lastChecked: Long? = -1,
    var largo: String? = "",
    var ancho: String? = "",
    var alto: String? = "",
    var peso: String? = "",
    var refCliente: String? = "",
    var codProducto: String? = "",
    var fechaCalculada: String? = " ",
    var notify: Boolean = true,
    @TypeConverters(UpdateStatusConverter::class)
    var updateStatus: UpdateStatus
) {

    fun containsDimensions(): Boolean =
        ancho?.isNotBlank() ?: false &&
        largo?.isNotBlank()?: false &&
        alto?.isNotBlank()?: false
    enum class UpdateStatus {
        UNKNOWN,
        OK,
        INPROGRESS,
        ERROR
    }

    enum class Stance {
        OUTGOING,
        INCOMING
    }
}
