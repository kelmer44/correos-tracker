package net.kelmer.correostracker.di.application

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import net.kelmer.correostracker.CorreosApp
import net.kelmer.correostracker.data.db.DbModule
import net.kelmer.correostracker.data.network.ApiModule
import net.kelmer.correostracker.data.repository.RepositoryModule
import net.kelmer.correostracker.data.usecases.UseCasesModule
import net.kelmer.correostracker.di.activity.ActivityBindings
import net.kelmer.correostracker.di.fragment.FragmentBindings
import net.kelmer.correostracker.di.modules.NetModule
import net.kelmer.correostracker.di.viewModel.ViewModelBuilder
import javax.inject.Singleton

/**
 * Created by gabriel on 25/03/2018.
 */
@Singleton
@Component(modules = [
    ApplicationModule::class,
    AndroidSupportInjectionModule::class,
    ViewModelBuilder::class,
    NetModule::class,
    DbModule::class,
    RepositoryModule::class,
    ApiModule::class,
    FragmentBindings::class,
    ActivityBindings::class,
    UseCasesModule::class
])
interface ApplicationComponent : AndroidInjector<CorreosApp> {


    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): ApplicationComponent
    }
//
//
//    fun injectTo(app: CorreosApp)
//    fun injectTo(app: ParcelListFragment)
//    fun injectTo(app: DetailFragment)
//    fun injectTo(viewModel: ParcelDetailViewModel)
//    fun injectTo(viewModel: ParcelListViewModel)
//
//    fun plus(createParcelModule: CreateParcelModule): CreateParcelComponent
//    fun plus(parcelDetailModule: ParcelDetailModule): ParcelDetailComponent
//    fun plus(serviceModule: ServiceModule): ServiceComponent

}