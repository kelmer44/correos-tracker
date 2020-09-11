package net.kelmer.correostracker.data.prefs

/**
 * Created by Gabriel Sanmartín on 11/09/2020.
 */
enum class ThemeMode(val idx: Int) {
    LIGHT(0),
    DARK(1),
    SYSTEM(2);

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