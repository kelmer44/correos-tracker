package net.kelmer.correostracker.ui.create

import dagger.Subcomponent
import net.kelmer.correostracker.di.FragmentScope

/**
 * Created by gabriel on 25/03/2018.
 */

@FragmentScope
@Subcomponent(modules = arrayOf(
        CreateParcelModule::class

))
interface CreateParcelComponent {

    fun injectTo(fragment: CreateParcelFragment)
    fun injectTo(viewModel: CreateParcelViewModel)
}