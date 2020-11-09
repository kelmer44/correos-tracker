package net.kelmer.correostracker.base.activity

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import net.kelmer.correostracker.R
import timber.log.Timber
import javax.inject.Inject

abstract class BaseActivity(@LayoutRes val layoutId: Int) : AppCompatActivity(){


    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.w("DUPLICATE - onCreated ${javaClass.simpleName}")
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
    }

}