package net.kelmer.correostracker.ui.create

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

/**
 * Created by gabriel on 25/03/2018.
 */
@Module
abstract class CreateParcelModule : ActivityModule<ParcelListActivity>() {

    @ContributesAndroidInjector
    internal abstract fun contributesCreateParcelFragment() : CreateParcelFragment

    @Binds
    @IntoMap
    @ViewModelKey(CreateParcelViewModel::class)
    @PerActivity
    internal abstract fun createParcelViewModel(createParcelViewModel: CreateParcelViewModel): ViewModel


}