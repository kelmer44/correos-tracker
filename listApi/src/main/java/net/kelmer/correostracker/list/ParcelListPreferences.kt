package net.kelmer.correostracker.list

import io.reactivex.Flowable
import kotlinx.coroutines.flow.StateFlow

interface ParcelListPreferences<T> {
    val themeModeStream: Flowable<T>
    var theme: T
    fun hasSeenFeatureBlurb(versionName: String): Boolean
    fun setSeenFeatureBlurb(versionName: String)
    var compactMode: Boolean
    val compactModeStream: Flowable<Boolean>
    var sortOrder: ParcelSortOrder
    val sortOrderStream: Flowable<ParcelSortOrder>
    /** Whether the user chose to stop seeing the "notifications may be delayed" battery hint. */
    fun isBatteryHintDismissed(): Boolean
    fun setBatteryHintDismissed()
}
