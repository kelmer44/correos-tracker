package net.kelmer.correostracker.data.prefs

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

class SharedPrefsManagerImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : SharedPrefsManager {

    init {
        incrementCleanStarts()
        Timber.w("Incrementing clean starts!, value after is = ${this.getCleanStarts()}")
    }

    private val _themeModeLive: MutableLiveData<Int> = MutableLiveData()
    override val themeModeLive: LiveData<Int>
        get() = _themeModeLive

    override var themeMode: Int = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        get() = sharedPreferences.getInt(PREFERENCE_NIGHT_MODE, PREFERENCE_NIGHT_MODE_DEF_VAL)
        set(value) {
            sharedPreferences.edit().putInt(
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
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangedListener)
    }

    override fun hasSeenFeatureBlurb(versionName: String) = get(FEATURE_SEEN + "_" + versionName, false)

    override fun setSeenFeatureBlurb(versionName: String) {
        set(FEATURE_SEEN + "_" + versionName, true)
    }

    override fun wasAskedForReview(): Boolean = get(ASKED_FOR_REVIEW, false)

    override fun markAskedForReview() {
        set(ASKED_FOR_REVIEW, true)
    }

    override fun getCleanStarts(): Int = get(CLEAN_STARTS, 0)

    override fun incrementCleanStarts() {
        set(CLEAN_STARTS, getCleanStarts() + 1)
    }

    /**
     * Put boolean in shared preference
     * @param key Key for boolean value
     * @param value value of boolean
     */
    operator fun set(key: String, value: Boolean) {
        sharedPreferences.edit()?.putBoolean(key, value)?.apply()
    }

    /**
     * Put String in shared preference
     * @param key Key for String value
     * @param value value of String
     */
    operator fun set(key: String, value: String) {
        sharedPreferences.edit()?.putString(key, value)?.apply()
    }

    /**
     * Put Int in shared preference
     * @param key Key for Int value
     * @param value value of Int
     */
    operator fun set(key: String, value: Int) {
        sharedPreferences.edit()?.putInt(key, value)?.apply()
    }

    /**
     * Get value for key in boolean format
     * @param key Key for boolean value
     * @param defaultValue If no value found for given key then return default value
     */
    operator fun get(key: String, defaultValue: Boolean): Boolean =
        sharedPreferences.getBoolean(key, defaultValue)

    /**
     * Get value for key in String format
     * @param key Key for String value
     * @param defaultValue If no value found for given key then return default value
     */
    operator fun get(key: String, defaultValue: String): String =
        sharedPreferences.getString(key, defaultValue)!!

    /**
     * Put long in shared preference
     * @param key Key for long value
     * @param value value of long
     */
    operator fun set(key: String, value: Long) {
        sharedPreferences.edit()?.putLong(key, value)?.apply()
    }

    /**
     * Get value for key in long format
     * @param key Key for long value
     * @param defaultValue If no value found for given key then return default value
     */
    operator fun get(key: String, defaultValue: Long): Long =
        sharedPreferences.getLong(key, defaultValue)

    /**
     * Get value for key in Int format
     * @param key Key for Int value
     * @param defaultValue If no value found for given key then return default value
     */
    operator fun get(key: String, defaultValue: Int): Int =
        sharedPreferences.getInt(key, defaultValue)

    /**
     * Clear SharedPreference
     */
    override fun clear() {
        sharedPreferences.edit()?.clear()?.apply()
    }

    companion object {
        private const val FEATURE_SEEN = "C_FEATURE_SEEN"
        private const val ASKED_FOR_REVIEW = "B_ASKED_FOR_REVIEW"
        private const val CLEAN_STARTS = "I_CLEAN_STARTS"
        private const val PREFERENCE_NIGHT_MODE = "preference_night_mode"
        private const val PREFERENCE_NIGHT_MODE_DEF_VAL = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }
}
