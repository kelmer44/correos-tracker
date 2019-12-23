package net.kelmer.correostracker.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.kelmer.correostracker.di.application.ApplicationComponent
import net.kelmer.correostracker.CorreosApp
import timber.log.Timber

/**
 * Created by gabriel on 25/03/2018.
 */
abstract class BaseFragment<V: ViewModel> : Fragment() {

    var isReady: Boolean = false
    abstract val viewModelClass: Class<V>
    abstract val layoutId : Int

    protected lateinit var viewModel: V

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater?.inflate(layoutId, container, false)
        return view
    }


    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.e("BF: onCreate " + javaClass.simpleName + "(" + this + ")")
        viewModel =
                ViewModelProviders.of(this).get(viewModelClass)
        injectDependencies(CorreosApp.graph)
    }

    abstract fun injectDependencies(graph: ApplicationComponent)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadUp()
        isReady = true
    }


    abstract fun loadUp()

}