package net.kelmer.correostracker.list

enum class ParcelSort {
    NAME,

    /** Date of the latest event. */
    DATE
}

/**
 * [ascending] means A to Z for [ParcelSort.NAME] and oldest first for [ParcelSort.DATE].
 */
data class ParcelSortOrder(
    val sort: ParcelSort = ParcelSort.NAME,
    val ascending: Boolean = true
)
