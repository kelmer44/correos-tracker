package net.kelmer.correostracker

import android.app.Application
import net.kelmer.correostracker.data.db.DbModule
import timber.log.Timber

/**
 * Created by gabriel on 25/03/2018.
 */
class CorreosApp : Application() {

    companion object {
        lateinit var graph: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()
        initDependencyGraph()
        setupTimber()
    }

    private fun setupTimber() {
        Timber.plant(Timber.DebugTree())
    }

//    private fun setupStetho() {
//        Stetho.initializeWithDefaults(this)
//    }

    private fun initDependencyGraph() {
        graph = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .dbModule(DbModule(this))
                .build()
        graph.injectTo(this)
    }
}