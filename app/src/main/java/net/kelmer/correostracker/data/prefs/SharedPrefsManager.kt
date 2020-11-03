package net.kelmer.correostracker.data.prefs

import androidx.lifecycle.LiveData

interface SharedPrefsManager {

    fun hasSeenFeatureBlurb(): Boolean
    fun setSeenFeatureBlurb()
    fun clear()
    fun setPreferredTheme(mode: ThemeMode)
    fun getPreferredTheme(): ThemeMode
    val isDarkThemeLive: LiveData<Boolean>
    var isDarkTheme: Boolean
    val nightModeLive: LiveData<Int>
}