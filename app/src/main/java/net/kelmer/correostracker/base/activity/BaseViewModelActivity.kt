package net.kelmer.correostracker.base.activity

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import net.kelmer.correostracker.base.BaseViewModel
import timber.log.Timber
import javax.inject.Inject

abstract class BaseViewModelActivity<E: BaseViewModel> : BaseActivity(){

    protected lateinit var viewModel: E

    abstract val viewModelClass: Class<E>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    abstract val layoutId: Int

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(viewModelClass)
        setContentView(layoutId)
        loadUp(savedInstanceState)
    }


    abstract fun loadUp(savedInstanceState: Bundle?)

}