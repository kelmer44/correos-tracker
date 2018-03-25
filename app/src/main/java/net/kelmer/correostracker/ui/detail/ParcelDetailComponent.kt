package net.kelmer.correostracker.ui.detail

import dagger.Subcomponent
import net.kelmer.correostracker.di.FragmentScope


@FragmentScope
@Subcomponent(modules = arrayOf(
        ParcelDetailModule::class

))
interface ParcelDetailComponent {

    fun injectTo(fragment: DetailFragment)
    fun injectTo(viewModel: ParcelDetailViewModel)
}