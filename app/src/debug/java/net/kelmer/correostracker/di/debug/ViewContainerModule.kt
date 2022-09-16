package net.kelmer.correostracker.di.debug

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.kelmer.correostracker.ui.debug.DebugViewContainer
import javax.inject.Singleton

/**
 * Created by Gabriel Sanmartín on 09/11/2020.
 */
@Module
@InstallIn(SingletonComponent::class)
class ViewContainerModule {

    @Provides
    @Singleton
    fun providesViewContainer(): ViewContainer = DebugViewContainer()
}
