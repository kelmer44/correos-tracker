package net.kelmer.correostracker.ui.detail

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import net.kelmer.correostracker.di.activity.ActivityModule
import net.kelmer.correostracker.ui.list.ParcelListActivity

@Module
@InstallIn(FragmentComponent::class)
abstract class ParcelDetailModule : ActivityModule<ParcelListActivity>() {




}