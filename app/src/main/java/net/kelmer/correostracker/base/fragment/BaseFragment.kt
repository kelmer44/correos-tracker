package net.kelmer.correostracker.base.fragment

import android.content.Context
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.viewbinding.ViewBinding
import timber.log.Timber

/**
 * Created by gabriel on 25/03/2018.
 */
abstract class BaseFragment<B : ViewBinding>(@LayoutRes layoutId: Int) : Fragment(layoutId) {

    override fun onAttach(context: Context) {
        Timber.w("FREEZE - onAttach $this ")
        super.onAttach(context)
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    var binding: B? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = bind(view)
        this.binding = binding
        loadUp(binding, savedInstanceState)
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }


    abstract fun bind(view: View): B
    abstract fun setupToolbar(toolbar: Toolbar)
    abstract fun loadUp(binding: B, savedInstanceState: Bundle?)

}