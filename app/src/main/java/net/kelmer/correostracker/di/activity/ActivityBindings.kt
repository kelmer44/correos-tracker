package net.kelmer.correostracker.di.activity

import dagger.Module
import dagger.android.ContributesAndroidInjector
import net.kelmer.correostracker.di.scopes.PerActivity
import net.kelmer.correostracker.ui.create.CreateActivity
import net.kelmer.correostracker.ui.create.CreateParcelModule
import net.kelmer.correostracker.ui.detail.DetailActivity
import net.kelmer.correostracker.ui.detail.ParcelDetailModule
import net.kelmer.correostracker.ui.list.ParcelListActivity
import net.kelmer.correostracker.ui.list.ParcelListModule

@Module
abstract class ActivityBindings {


    @ContributesAndroidInjector(modules = [ParcelListModule::class])
    @PerActivity
    internal abstract fun parcelListActivity(): ParcelListActivity


    @ContributesAndroidInjector(modules = [CreateParcelModule::class])
    @PerActivity
    internal abstract fun createActivity(): CreateActivity


    @ContributesAndroidInjector(modules = [ParcelDetailModule::class])
    @PerActivity
    internal abstract fun detailActivity(): DetailActivity
}