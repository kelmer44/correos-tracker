package net.kelmer.correostracker.fase

import net.kelmer.correostracker.core.R
import androidx.annotation.DrawableRes

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
