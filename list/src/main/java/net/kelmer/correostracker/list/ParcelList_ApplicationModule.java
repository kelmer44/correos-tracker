package net.kelmer.correostracker.list;

import net.kelmer.correostracker.list.preferences.ParcelListPreferencesImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
abstract class ParcelList_ApplicationModule {

   @Binds
   abstract ParcelListPreferences bindParcelListPreferences(ParcelListPreferencesImpl impl);
}
