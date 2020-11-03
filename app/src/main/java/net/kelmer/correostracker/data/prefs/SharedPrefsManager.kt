package net.kelmer.correostracker.data.prefs

import androidx.lifecycle.LiveData

interface SharedPrefsManager {
    fun hasSeenFeatureBlurb(): Boolean
    fun setSeenFeatureBlurb()
    fun clear()
    var themeMode: Int
    val themeModeLive: LiveData<Int>
}