package net.kelmer.correostracker.list

import androidx.lifecycle.LiveData

interface ParcelListPreferences {
    val themeModeLive: LiveData<Int>
}
