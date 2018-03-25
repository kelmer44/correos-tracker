package net.kelmer.correostracker.base

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.Fragment
import android.view.View
import timber.log.Timber

/**
 * Created by gabriel on 25/03/2018.
 */
abstract class BaseFragment<V: ViewModel> : Fragment() {

    var isReady: Boolean = false
    abstract val viewModelClass: Class<V>
    protected lateinit var viewModel: V

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.e("BF: onCreate " + javaClass.simpleName + "(" + this + ")")
        viewModel =
                ViewModelProviders.of(this).get(viewModelClass)
//        injectDependencies(MycujooPlayerApp.graph)

    }
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadUp()
        isReady = true
    }


    abstract fun loadUp()

}