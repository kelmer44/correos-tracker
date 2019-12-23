package net.kelmer.correostracker.ui.detail

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import net.kelmer.correostracker.di.activity.ActivityModule
import net.kelmer.correostracker.di.fragment.CommonFragmentModule
import net.kelmer.correostracker.di.fragment.FragmentModule
import net.kelmer.correostracker.di.scopes.PerActivity
import net.kelmer.correostracker.di.scopes.PerFragment
import net.kelmer.correostracker.di.viewModel.ViewModelKey
import net.kelmer.correostracker.ui.list.ParcelListActivity
import net.kelmer.correostracker.ui.list.ParcelListFragment
import net.kelmer.correostracker.ui.list.ParcelListViewModel

@Module
abstract class ParcelDetailModule : ActivityModule<ParcelListActivity>() {


    @ContributesAndroidInjector
    internal abstract fun contributesDetailFragment() : DetailFragment

    @Binds
    @IntoMap
    @ViewModelKey(ParcelDetailViewModel::class)
    @PerActivity
    internal abstract fun parcelDetailViewModel(parcelDetailViewModel: ParcelDetailViewModel): ViewModel


}