package net.kelmer.correostracker.ui.theme

import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import net.kelmer.correostracker.theme.R

/**
 * Created by Gabriel Sanmartín on 11/09/2020.
 */
enum class ThemeMode(val code: Int, @StringRes val stringRes: Int) {
    LIGHT(AppCompatDelegate.MODE_NIGHT_NO, R.string.theme_light),
    DARK(AppCompatDelegate.MODE_NIGHT_YES, R.string.theme_dark),
    SYSTEM(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, R.string.theme_system);

    companion object {

        fun fromOrdinal(number: Int): ThemeMode = values()[number]
    }
}
