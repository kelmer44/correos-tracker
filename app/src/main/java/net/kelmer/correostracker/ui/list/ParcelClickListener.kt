package net.kelmer.correostracker.ui.list

import android.view.View
import net.kelmer.correostracker.data.model.local.LocalParcelReference

/**
 * Created by gabriel on 25/03/2018.
 */
interface ParcelClickListener {

    fun click(parcelReference: LocalParcelReference)

    fun dots(view: View, position: LocalParcelReference)

    fun update(parcelReference: LocalParcelReference)

    fun longPress(parcelReference: LocalParcelReference)

}