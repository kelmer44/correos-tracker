package net.kelmer.correostracker.list;

import android.app.Application;
import android.app.UiModeManager;
import android.content.Context;

import androidx.fragment.app.Fragment;

import net.kelmer.correostracker.list.preferences.ParcelListPreferencesImpl;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
abstract class ParcelList_ApplicationModule {

    @Binds
    abstract ParcelListPreferences bindParcelListPreferences(ParcelListPreferencesImpl impl);
}
