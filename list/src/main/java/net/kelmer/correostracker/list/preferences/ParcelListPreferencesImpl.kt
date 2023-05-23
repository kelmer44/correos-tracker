package net.kelmer.correostracker.list.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor
import kotlinx.coroutines.flow.MutableStateFlow
import net.kelmer.correostracker.dataApi.LazySharedPreferences
import net.kelmer.correostracker.list.ParcelListPreferences
import net.kelmer.correostracker.ui.theme.ThemeMode
import timber.log.Timber
import javax.inject.Inject
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ParcelListPreferencesImpl @Inject constructor(@ApplicationContext context: Context) :
    LazySharedPreferences(
        context,
        PARCELLIST_PREFS_KEY
    ), ParcelListPreferences<ThemeMode> {

    override var theme: ThemeMode by ThemeModePreferenceDelegate(PREFERENCE_NIGHT_MODE, ThemeMode.SYSTEM)
    override var compactMode: Boolean by CompactModeDelegate(false)

    private val themeProcessor = BehaviorProcessor.createDefault(theme)
    private val compactModeProcessor = BehaviorProcessor.createDefault(compactMode)
    override val themeModeStream: Flowable<ThemeMode> = themeProcessor
    override val compactModeStream: Flowable<Boolean> = compactModeProcessor
    override fun hasSeenFeatureBlurb(versionName: String) = get(FEATURE_SEEN + "_" + versionName, false)

    override fun setSeenFeatureBlurb(versionName: String) {
        set(FEATURE_SEEN + "_" + versionName, true)
    }

    inner class ThemeModePreferenceDelegate(
        private val name: String,
        private val default: ThemeMode,
    ) : ReadWriteProperty<Any?, ThemeMode> {

        override fun getValue(thisRef: Any?, property: KProperty<*>): ThemeMode =
            ThemeMode.fromPosition(getInt(name, default.ordinal))

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: ThemeMode) {
            themeProcessor.onNext(value)
            this@ParcelListPreferencesImpl.edit { putInt(name, value.ordinal) }
        }
    }

    inner class CompactModeDelegate(
        private val default: Boolean,
    ) : ReadWriteProperty<Any?, Boolean> {

        override fun getValue(thisRef: Any?, property: KProperty<*>): Boolean =
            getBoolean(COMPACT_MODE, default)

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) {
            compactModeProcessor.onNext(value)
            this@ParcelListPreferencesImpl.edit { putBoolean(COMPACT_MODE, value) }
        }
    }

    companion object {
        const val PARCELLIST_PREFS_KEY = "ParcelList"
        private const val FEATURE_SEEN = "C_FEATURE_SEEN"
        private const val COMPACT_MODE = "C_COMPACT_MODE"
        private const val PREFERENCE_NIGHT_MODE = "preference_night_mode"
    }
}
