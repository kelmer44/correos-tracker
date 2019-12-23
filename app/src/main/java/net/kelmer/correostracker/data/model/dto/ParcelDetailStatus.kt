package net.kelmer.correostracker.data.model.dto

import androidx.annotation.DrawableRes
import net.kelmer.correostracker.R


data class ParcelDetailStatus(val fecEvento: String,
                              val horEvento: String,
                              val fase: String,
                              val desTextoResumen: String,
                              val desTextoAmpliado: String)

{

    enum class Fase(val faseNumber: Int, @DrawableRes val drawable: Int){
        PRE(0,R.drawable.timeline_circle_pre),
        ENCAMINO(1, R.drawable.timeline_circle_encamino),
        REPARTO(2, R.drawable.timeline_circle_reparto),
        ENTREGADO(3, R.drawable.timeline_circle_entregado),
        OTHER(-1, R.drawable.timeline_circle);

        companion object {
            fun fromFase(fase: Int): Fase {
                Fase.values().forEach {
                    if(it.faseNumber == fase){
                        return it
                    }
                }
                return OTHER
            }
        }
    }


}