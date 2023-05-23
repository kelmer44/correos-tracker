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
}
