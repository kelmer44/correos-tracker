package net.kelmer.correostracker.data.prefs

import androidx.annotation.StringRes
import net.kelmer.correostracker.R

/**
 * Created by Gabriel Sanmartín on 11/09/2020.
 */
enum class ThemeMode(val idx: Int,  @StringRes val stringRes: Int) {
    LIGHT(0, R.string.theme_light),
    DARK(1, R.string.theme_dark),
    SYSTEM(2, R.string.theme_system);

    companion object {

        fun fromIdx(number: Int): ThemeMode {
            ThemeMode.values().forEach {
                if (it.idx == number)
                    return it
            }
            return SYSTEM
        }
    }
}