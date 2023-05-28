package net.kelmer.correostracker.initializer;

import androidx.lifecycle.LifecycleObserver;

import net.kelmer.correostracker.AppInitializer;

import java.util.Set;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import dagger.multibindings.IntoSet;
import dagger.multibindings.Multibinds;

@Module
@InstallIn(SingletonComponent.class)
abstract class Initializer_AppModule {

   @Binds
   @IntoSet
   abstract AppInitializer bindsNotificationInitializer(NotificationInitializer impl);

   @Binds
   @IntoSet
   abstract AppInitializer bindsTimberInitializer(TimberInitializer impl);

   @Binds
   @IntoSet
   abstract AppInitializer bindsCrashlyticsInitializer(CrashlyticsInitializer impl);

   @Binds
   @IntoSet
   abstract AppInitializer bindsWorkerInitializer(WorkerFactoryInitializer impl);

   @Binds
   @IntoSet
   abstract AppInitializer bindsAdsInitializer(AdsInitializer impl);
}
