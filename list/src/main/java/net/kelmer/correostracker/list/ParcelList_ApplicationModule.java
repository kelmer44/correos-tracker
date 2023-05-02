package net.kelmer.correostracker.list;

import net.kelmer.correostracker.list.preferences.ParcelListPreferencesImpl;
import net.kelmer.correostracker.ui.theme.ThemeMode;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
abstract class ParcelList_ApplicationModule {
    @Binds
    @Singleton
    abstract ParcelListPreferences<ThemeMode> bindParcelListPreferences(ParcelListPreferencesImpl impl);

}
