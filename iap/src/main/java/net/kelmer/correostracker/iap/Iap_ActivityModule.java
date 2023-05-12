package net.kelmer.correostracker.iap;

import androidx.lifecycle.LifecycleObserver;

import net.kelmer.correostracker.ActivityLifecycleObserver;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.multibindings.IntoSet;

@Module
@InstallIn(ActivityComponent.class)
abstract class Iap_ActivityModule {

   @Binds
   @IntoSet
   @ActivityLifecycleObserver
   abstract LifecycleObserver bindsBillingLifecycleObserver(BillingClientProvider impl);
}
