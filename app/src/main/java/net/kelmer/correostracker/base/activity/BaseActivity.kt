package net.kelmer.correostracker.base.activity

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(){


    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.w("DUPLICATE - onCreated ${javaClass.simpleName}")
        super.onCreate(savedInstanceState)
    }

}