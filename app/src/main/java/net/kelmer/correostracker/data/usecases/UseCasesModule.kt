package net.kelmer.correostracker.data.usecases

import dagger.Binds
import dagger.Module

@Module
abstract class UseCasesModule {

    @Binds
    abstract fun createParcel(
        createParcelImpl: CreateParcelImpl
    ): CreateParcel
}