package net.kelmer.correostracker.initializer;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import dagger.multibindings.IntoSet;

@Module
@InstallIn(SingletonComponent.class)
abstract class Initializer_AppModule {

   @Binds
   @IntoSet
   abstract Initializer bindsNotificationInitializer(NotificationInitializer impl);

   @Binds
   @IntoSet
   abstract Initializer bindsTimberInitializer(TimberInitializer impl);

   @Binds
   @IntoSet
   abstract Initializer bindsCrashlyticsInitializer(CrashlyticsInitializer impl);

   @Binds
   @IntoSet
   abstract Initializer bindsWorkerInitializer(WorkerFactoryInitializer impl);

   @Binds
   @IntoSet
   abstract Initializer bindsAdsInitializer(AdsInitializer impl);
}
