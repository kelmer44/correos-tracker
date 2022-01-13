package net.kelmer.correostracker.data.prefs

import androidx.lifecycle.LiveData

interface SharedPrefsManager {
    fun hasSeenFeatureBlurb(versionName: String): Boolean
    fun setSeenFeatureBlurb(versionName: String)

    fun wasAskedForReview(): Boolean
    fun markAskedForReview()

    fun getCleanStarts(): Int
    fun incrementCleanStarts()

    fun clear()
    var themeMode: Int
    val themeModeLive: LiveData<Int>
}
