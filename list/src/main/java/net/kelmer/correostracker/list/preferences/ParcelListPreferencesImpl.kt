package net.kelmer.correostracker.list.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import net.kelmer.correostracker.data.LazySharedPreferences
import net.kelmer.correostracker.list.ParcelListPreferences
import javax.inject.Inject

class ParcelListPreferencesImpl @Inject constructor(@ApplicationContext context: Context) :
    LazySharedPreferences(
        context,
        PARCELLIST_PREFS_KEY
    ), ParcelListPreferences {

    private val _themeModeLive: MutableLiveData<Int> = MutableLiveData()
    override val themeModeLive: LiveData<Int>
        get() = _themeModeLive

    var themeMode: Int = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        get() = getInt(PREFERENCE_NIGHT_MODE, PREFERENCE_NIGHT_MODE_DEF_VAL)
        set(value) {
            edit().putInt(
                PREFERENCE_NIGHT_MODE, value
            ).apply()
            field = value
        }

    private val preferenceChangedListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            when (key) {
                PREFERENCE_NIGHT_MODE -> {
                    _themeModeLive.value = themeMode
                }
            }
        }

    init {
        _themeModeLive.value = themeMode
        registerOnSharedPreferenceChangeListener(preferenceChangedListener)
    }


    fun hasSeenFeatureBlurb(versionName: String) = get(FEATURE_SEEN + "_" + versionName, false)

    fun setSeenFeatureBlurb(versionName: String) {
        set(FEATURE_SEEN + "_" + versionName, true)
    }


    companion object {
        const val PARCELLIST_PREFS_KEY = "ParcelList"
        private const val FEATURE_SEEN = "C_FEATURE_SEEN"
        private const val PREFERENCE_NIGHT_MODE = "preference_night_mode"
        private const val PREFERENCE_NIGHT_MODE_DEF_VAL = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }
}
