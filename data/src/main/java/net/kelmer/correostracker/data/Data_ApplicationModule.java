package net.kelmer.correostracker.data;

import net.kelmer.correostracker.data.remote.CorreosApi;
import net.kelmer.correostracker.data.remote.CorreosV1;
import net.kelmer.correostracker.data.remote.UnidadesApi;
import net.kelmer.correostracker.dataApi.repository.correos.CorreosRepository;
import net.kelmer.correostracker.dataApi.repository.local.LocalParcelRepository;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;

@Module
@InstallIn(SingletonComponent.class)
abstract class Data_ApplicationModule {

    @Binds
    abstract LocalParcelRepository bindsLocalParcelRepository(LocalParcelRepositoryImpl repo);

    @Binds
    abstract CorreosRepository bindsCorreosRepository(CorreosRepositoryImpl repo);

    @Singleton
    @Provides
    static CorreosV1 providesCorreosV1(@Named("parcelService") Retrofit retrofit) {
        return retrofit.create(CorreosV1.class);
    }

    @Singleton
    @Provides
    static CorreosApi providesCorreosApi(@Named("oldApi") Retrofit retrofit) {
        return retrofit.create(CorreosApi.class);
    }

    @Singleton
    @Provides
    static UnidadesApi providesUnidadesApi(@Named("unidadService") Retrofit retrofit) {
        return retrofit.create(UnidadesApi.class);
    }
}
