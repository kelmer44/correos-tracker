package net.kelmer.correostracker

import dagger.Component
import javax.inject.Singleton

/**
 * Created by gabriel on 25/03/2018.
 */
@Singleton
@Component(modules = [
ApplicationModule::class
])
interface ApplicationComponent {
    fun injectTo(app: CorreosApp)
}