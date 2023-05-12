package net.kelmer.correostracker.iap;

import androidx.lifecycle.LifecycleObserver;

import net.kelmer.correostracker.ActivityLifecycleObserver;
import net.kelmer.correostracker.ProcessLifecycleObserver;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import dagger.multibindings.IntoSet;

@Module
@InstallIn(SingletonComponent.class)
abstract class Iap_AppModule {

   @Binds
   @IntoSet
   @ProcessLifecycleObserver
   abstract LifecycleObserver bindsBillingLifecycleObserver(BillingClientProvider impl);

   @Binds
   abstract IapApi iapApi(BillingClientProvider impl);
}
