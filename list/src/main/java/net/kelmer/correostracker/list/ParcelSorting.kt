package net.kelmer.correostracker.list

import net.kelmer.correostracker.dataApi.model.local.LocalParcelReference
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.Locale

// dd/MM/yyyy is what Correos returns; the ISO forms are in rows saved by older versions.
private val EVENT_FORMATS = listOf(
    "dd/MM/yyyy HH:mm:ss",
    "dd/MM/yyyy HH:mm",
    "dd/MM/yyyy",
    "yyyy-MM-dd HH:mm:ss",
    "yyyy-MM-dd HH:mm",
    "yyyy-MM-dd"
)

/** Time of the parcel's latest event, or null if it has none or the date can't be parsed. */
fun LocalParcelReference.lastEventMillis(): Long? {
    val event = ultimoEstado ?: return null
    val text = "${event.fecEvento} ${event.horEvento}".trim()
    for (pattern in EVENT_FORMATS) {
        val format = SimpleDateFormat(pattern, Locale.ROOT).apply { isLenient = false }
        val position = ParsePosition(0)
        val date = format.parse(text, position)
        if (date != null && position.index == text.length) return date.time
    }
    return null
}

/** Parcels without a usable date always go last, whatever the direction. */
fun List<LocalParcelReference>.sortedFor(order: ParcelSortOrder): List<LocalParcelReference> {
    val byName = compareBy<LocalParcelReference, String>(String.CASE_INSENSITIVE_ORDER) { it.parcelName }
    return when (order.sort) {
        ParcelSort.NAME -> sortedWith(if (order.ascending) byName else byName.reversed())
        ParcelSort.DATE -> {
            val times = associate { it.code to it.lastEventMillis() }
            val (dated, undated) = partition { times[it.code] != null }
            val byTime = compareBy<LocalParcelReference> { times.getValue(it.code) }
            dated.sortedWith((if (order.ascending) byTime else byTime.reversed()).then(byName)) +
                undated.sortedWith(byName)
        }
    }
}
