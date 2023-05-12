package net.kelmer.correostracker.ui.activity;

import androidx.lifecycle.LifecycleObserver;

import net.kelmer.correostracker.ActivityLifecycleObserver;

import java.util.Set;

import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.multibindings.Multibinds;

@Module
@InstallIn(ActivityComponent.class)
abstract class MainActivity_ActivityModule {

   @Multibinds
   @ActivityLifecycleObserver
   abstract Set<LifecycleObserver> processLifecycleObservers();
}
