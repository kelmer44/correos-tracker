package net.kelmer.correostracker.di.service

import dagger.Subcomponent
import net.kelmer.correostracker.service.ParcelCheckerService

@Subcomponent(modules = arrayOf(ServiceModule::class))
interface ServiceComponent {
    fun inject(parcelCheckerService: ParcelCheckerService)
}