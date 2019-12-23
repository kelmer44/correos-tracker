package net.kelmer.correostracker.base

import android.app.IntentService
import androidx.annotation.UiThread
import net.kelmer.correostracker.CorreosApp
import net.kelmer.correostracker.di.service.ServiceComponent
import net.kelmer.correostracker.di.service.ServiceModule

abstract class BaseService(name: String) : IntentService(name) {

}