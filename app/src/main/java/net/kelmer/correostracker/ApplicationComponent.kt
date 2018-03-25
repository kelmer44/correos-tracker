package net.kelmer.correostracker

import dagger.Component
import net.kelmer.correostracker.data.db.DbModule
import net.kelmer.correostracker.data.network.ApiModule
import net.kelmer.correostracker.data.repository.RepositoryModule
import net.kelmer.correostracker.di.NetModule
import net.kelmer.correostracker.ui.create.CreateParcelComponent
import net.kelmer.correostracker.ui.create.CreateParcelModule
import net.kelmer.correostracker.ui.list.ParcelListFragment
import net.kelmer.correostracker.ui.list.ParcelListViewModel
import javax.inject.Singleton

/**
 * Created by gabriel on 25/03/2018.
 */
@Singleton
@Component(modules = [
ApplicationModule::class,
NetModule::class,
DbModule::class,
RepositoryModule::class,
ApiModule::class
])
interface ApplicationComponent {

    fun injectTo(app: CorreosApp)
    fun injectTo(app: ParcelListFragment)
    fun injectTo(viewModel: ParcelListViewModel)

    fun plus(createParcelModule: CreateParcelModule): CreateParcelComponent

}