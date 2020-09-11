package net.kelmer.correostracker.data.prefs

interface SharedPrefsManager {

    fun hasSeenFeatureBlurb(): Boolean
    fun setSeenFeatureBlurb()
    fun clear()
    fun setPreferredTheme(mode: ThemeMode)
    fun getPreferredTheme(): ThemeMode
}