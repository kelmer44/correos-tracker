package net.kelmer.correostracker.deviceinfo;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
abstract class Core_ApplicationModule {

   @Binds
   abstract DeviceInfo bindsDeviceInfo(DeviceInfoImpl impl);
}
