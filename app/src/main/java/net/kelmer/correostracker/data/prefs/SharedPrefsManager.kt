package net.kelmer.correostracker.data.prefs

import androidx.lifecycle.LiveData

interface SharedPrefsManager {
    fun hasSeenFeatureBlurb(versionCode: Int): Boolean
    fun setSeenFeatureBlurb(versionCode: Int)
    fun clear()
    var themeMode: Int
    val themeModeLive: LiveData<Int>
}