package net.kelmer.correostracker.data.prefs

import androidx.lifecycle.LiveData

interface SharedPrefsManager {
    fun hasSeenFeatureBlurb(versionName: String): Boolean
    fun setSeenFeatureBlurb(versionName: String)
    fun clear()
    var themeMode: Int
    val themeModeLive: LiveData<Int>
}
