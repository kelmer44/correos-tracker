package net.kelmer.correostracker.base.fragment

import android.content.Context
import androidx.lifecycle.ViewModel
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import android.view.View
import androidx.annotation.LayoutRes
import timber.log.Timber

/**
 * Created by gabriel on 25/03/2018.
 */
abstract class BaseFragment(@LayoutRes layoutId: Int) : Fragment(layoutId) {


    override fun onAttach(context: Context) {
        Timber.w("FREEZE - onAttach $this ")
        super.onAttach(context)
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadUp(savedInstanceState)
    }


    abstract fun loadUp(savedInstanceState: Bundle?)

}