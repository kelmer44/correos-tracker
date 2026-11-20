package net.kelmer.correostracker

import net.kelmer.correostracker.dataApi.model.local.LocalParcelReference
import net.kelmer.correostracker.dataApi.model.remote.CorreosApiEvent
import net.kelmer.correostracker.list.ParcelSort
import net.kelmer.correostracker.list.ParcelSortOrder
import net.kelmer.correostracker.list.lastEventMillis
import net.kelmer.correostracker.list.sortedFor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ParcelSortingTest {

    private fun parcel(name: String, date: String? = null, time: String = "10:00:00") =
        LocalParcelReference(
            code = name,
            trackingCode = name,
            parcelName = name,
            ultimoEstado = date?.let { CorreosApiEvent(fecEvento = it, horEvento = time) },
            updateStatus = LocalParcelReference.UpdateStatus.OK
        )

    private val dated = listOf(
        parcel("none"),
        parcel("old", "19/03/2018"),
        parcel("new", "02/11/2026"),
        parcel("newSameDayEarlier", "02/11/2026", "08:00:00")
    )

    private fun names(list: List<LocalParcelReference>) = list.map { it.parcelName }

    @Test
    fun nameSortIsCaseInsensitive() {
        val list = listOf(parcel("banana"), parcel("Cherry"), parcel("apple"))
        assertEquals(
            listOf("apple", "banana", "Cherry"),
            names(list.sortedFor(ParcelSortOrder(ParcelSort.NAME, ascending = true)))
        )
        assertEquals(
            listOf("Cherry", "banana", "apple"),
            names(list.sortedFor(ParcelSortOrder(ParcelSort.NAME, ascending = false)))
        )
    }

    @Test
    fun dateSortDescendingIsNewestFirst() {
        assertEquals(
            listOf("new", "newSameDayEarlier", "old", "none"),
            names(dated.sortedFor(ParcelSortOrder(ParcelSort.DATE, ascending = false)))
        )
    }

    @Test
    fun dateSortAscendingIsOldestFirstAndUndatedStillLast() {
        assertEquals(
            listOf("old", "newSameDayEarlier", "new", "none"),
            names(dated.sortedFor(ParcelSortOrder(ParcelSort.DATE, ascending = true)))
        )
    }

    @Test
    fun dateTiesFallBackToName() {
        val sorted = listOf(parcel("b", "01/01/2026"), parcel("a", "01/01/2026"))
            .sortedFor(ParcelSortOrder(ParcelSort.DATE, ascending = false))
        assertEquals(listOf("a", "b"), names(sorted))
    }

    @Test
    fun parsesSupportedFormats() {
        assertNotNull(parcel("a", "19/03/2018", "19:00:16").lastEventMillis())
        assertNotNull(parcel("a", "19/03/2018", "16:32").lastEventMillis())
        assertNotNull(parcel("a", "2020-01-02", "22:00").lastEventMillis())
        assertNotNull(parcel("a", "19/03/2018", "").lastEventMillis())
    }

    @Test
    fun unparseableOrMissingDateIsNull() {
        assertNull(parcel("a", "garbage").lastEventMillis())
        assertNull(parcel("a").lastEventMillis())
    }
}
