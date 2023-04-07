package net.kelmer.correostracker.ui

import androidx.annotation.DrawableRes
import net.kelmer.correostracker.theme.R

enum class Fase(val faseNumber: Int, @DrawableRes val drawable: Int) {
    PRE(1, R.drawable.timeline_icon_pre),
    ENCAMINO(2, R.drawable.timeline_icon_encamino),
    REPARTO(3, R.drawable.timeline_icon_reparto),

    ENTREGADO(4, R.drawable.timeline_icon_entregado),
    OTHER(-1, R.drawable.timeline_icon_unknown),
    ERROR(-2, R.drawable.timeline_icon_error);

    companion object {
        fun fromFase(fase: Int): Fase {
            Fase.values().forEach {
                if (it.faseNumber == fase) {
                    return it
                }
            }
            return OTHER
        }
    }
}
