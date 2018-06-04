package net.kelmer.correostracker.base

import android.app.IntentService
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.support.annotation.UiThread
import net.kelmer.correostracker.CorreosApp
import net.kelmer.correostracker.di.ServiceComponent
import net.kelmer.correostracker.di.ServiceModule

abstract class BaseService(name: String) : IntentService(name) {

    @UiThread
    protected fun getServiceComponent(): ServiceComponent {
        val serviceComponent = CorreosApp.graph.plus(ServiceModule(this))
        return serviceComponent
    }
}