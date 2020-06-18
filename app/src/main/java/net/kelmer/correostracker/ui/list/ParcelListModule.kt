package net.kelmer.correostracker.ui.list

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import net.kelmer.correostracker.di.activity.ActivityModule

@Module
@InstallIn(ActivityComponent::class)
abstract class ParcelListModule : ActivityModule<ParcelListActivity>() {


}