package net.kelmer.correostracker.ui.list

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import net.kelmer.correostracker.di.activity.ActivityModule
import net.kelmer.correostracker.di.scopes.PerActivity
import net.kelmer.correostracker.di.viewModel.ViewModelKey

@Module
abstract class ParcelListModule : ActivityModule<ParcelListActivity>() {

    @ContributesAndroidInjector
    internal abstract fun contributesParcelListFragment() : ParcelListFragment

    @Binds
    @IntoMap
    @ViewModelKey(ParcelListViewModel::class)
    @PerActivity
    internal abstract fun parcelListViewModel(parcelListViewModel: ParcelListViewModel): ViewModel

}