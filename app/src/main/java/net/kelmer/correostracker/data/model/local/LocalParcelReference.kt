package net.kelmer.correostracker.data.model.local

import android.arch.persistence.room.*
import net.kelmer.correostracker.data.model.remote.CorreosApiEvent
import java.util.*

/**
 * Created by gabriel on 25/03/2018.
 */
@Entity
data class LocalParcelReference(
        @PrimaryKey(autoGenerate = false)
        var code: String = "",
        var parcelName: String = "",
        @TypeConverters(StanceConverter::class)
        var stance: Stance = Stance.INCOMING,
        @Embedded
        var ultimoEstado: CorreosApiEvent? = null,
        @Ignore
        var isLoading: Boolean = false,
        var lastChecked: Long? = -1) {


    enum class Stance {
        OUTGOING,
        INCOMING
    }
}