package net.kelmer.correostracker.data.model.dto

import androidx.annotation.DrawableRes
import net.kelmer.correostracker.dataApi.R

data class ParcelDetailStatus(
    val fecEvento: String,
    val horEvento: String,
    val fase: String,
    val desTextoResumen: String,
    val desTextoAmpliado: String
) {

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
}
