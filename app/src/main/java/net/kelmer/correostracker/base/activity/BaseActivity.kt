package net.kelmer.correostracker.base.activity

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), HasAndroidInjector{


    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>
    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.w("DUPLICATE - onCreated ${javaClass.simpleName}")
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

}