package net.kelmer.correostracker.initializer

import android.app.Application

interface Initializer {

    fun initialize(application: Application)
}
