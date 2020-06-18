package net.kelmer.correostracker.ui.create

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import net.kelmer.correostracker.di.activity.ActivityModule
import net.kelmer.correostracker.ui.list.ParcelListActivity

/**
 * Created by gabriel on 25/03/2018.
 */
@Module
@InstallIn(FragmentComponent::class)
abstract class CreateParcelModule : ActivityModule<ParcelListActivity>() {


}