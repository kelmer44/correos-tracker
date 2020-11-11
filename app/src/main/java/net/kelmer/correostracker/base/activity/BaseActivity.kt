package net.kelmer.correostracker.base.activity

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import net.kelmer.correostracker.di.debug.ViewContainer
import timber.log.Timber
import javax.inject.Inject

abstract class BaseActivity(@LayoutRes val layoutId: Int) : AppCompatActivity() {

    @Inject
    lateinit var viewContainer: ViewContainer

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.w("DUPLICATE - onCreated ${javaClass.simpleName}")
        super.onCreate(savedInstanceState)
        val container = viewContainer.forActivity(this)
        layoutInflater.inflate(layoutId, container, true)
    }
}
