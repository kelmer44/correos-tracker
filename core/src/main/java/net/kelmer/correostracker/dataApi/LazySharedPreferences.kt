package net.kelmer.correostracker.dataApi

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import net.kelmer.correostracker.core.BuildConfig

/**
 * Lazy implementation of [SharedPreferences] that will only obtain an instance once a value gets requested. This class
 * was added to allow injection of [SharedPreferences] as a dependency without disk IO.
 */
open class LazySharedPreferences constructor(
    private val context: Context,
    private val name: String,
) : SharedPreferences {

    private val preferences by lazy {
        context.getSharedPreferences(name + if(BuildConfig.DEBUG) "_DEBUG" else "", Context.MODE_PRIVATE)
    }

    override fun contains(key: String?): Boolean = preferences.contains(key)

    override fun registerOnSharedPreferenceChangeListener(listener: OnSharedPreferenceChangeListener?) =
        preferences.registerOnSharedPreferenceChangeListener(listener)

    override fun unregisterOnSharedPreferenceChangeListener(
        listener: OnSharedPreferenceChangeListener?
    ) =
        preferences.unregisterOnSharedPreferenceChangeListener(listener)

    override fun getBoolean(key: String?, defValue: Boolean): Boolean = preferences.getBoolean(key, defValue)

    override fun getInt(key: String?, defValue: Int): Int = preferences.getInt(key, defValue)

    override fun getAll(): MutableMap<String, *> = preferences.all

    override fun edit(): Editor = preferences.edit()

    override fun getLong(key: String?, defValue: Long): Long = preferences.getLong(key, defValue)

    override fun getFloat(key: String?, defValue: Float): Float = preferences.getFloat(key, defValue)

    override fun getStringSet(key: String?, defValues: MutableSet<String>?): MutableSet<String>? =
        preferences.getStringSet(key, defValues)

    override fun getString(key: String?, defValue: String?): String? = preferences.getString(key, defValue)

   /**
    * Put boolean in shared preference
    * @param key Key for boolean value
    * @param value value of boolean
    */
    operator fun set(key: String, value: Boolean) {
       preferences.edit()?.putBoolean(key, value)?.apply()
    }

    /**
     * Put String in shared preference
     * @param key Key for String value
     * @param value value of String
     */
    operator fun set(key: String, value: String) {
        preferences.edit()?.putString(key, value)?.apply()
    }

    /**
     * Put Int in shared preference
     * @param key Key for Int value
     * @param value value of Int
     */
    operator fun set(key: String, value: Int) {
        preferences.edit()?.putInt(key, value)?.apply()
    }

    /**
     * Get value for key in boolean format
     * @param key Key for boolean value
     * @param defaultValue If no value found for given key then return default value
     */
    operator fun get(key: String, defaultValue: Boolean): Boolean =
        preferences.getBoolean(key, defaultValue)

    /**
     * Get value for key in String format
     * @param key Key for String value
     * @param defaultValue If no value found for given key then return default value
     */
    operator fun get(key: String, defaultValue: String): String =
        preferences.getString(key, defaultValue)!!

    /**
     * Put long in shared preference
     * @param key Key for long value
     * @param value value of long
     */
    operator fun set(key: String, value: Long) {
        preferences.edit()?.putLong(key, value)?.apply()
    }

    /**
     * Get value for key in long format
     * @param key Key for long value
     * @param defaultValue If no value found for given key then return default value
     */
    operator fun get(key: String, defaultValue: Long): Long =
        preferences.getLong(key, defaultValue)

    /**
     * Get value for key in Int format
     * @param key Key for Int value
     * @param defaultValue If no value found for given key then return default value
     */
    operator fun get(key: String, defaultValue: Int): Int =
        preferences.getInt(key, defaultValue)
}
