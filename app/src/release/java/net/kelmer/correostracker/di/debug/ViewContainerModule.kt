package net.kelmer.correostracker.di.debug

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn

/**
 * Created by Gabriel Sanmartín on 09/11/2020.
 */
@Module
@InstallIn(dagger.hilt.components.SingletonComponent::class)
class ViewContainerModule {

    @Provides
    fun providesViewContainer(): ViewContainer = ViewContainer.DEFAULT
}
