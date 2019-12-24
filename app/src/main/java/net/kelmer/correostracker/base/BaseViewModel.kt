package net.kelmer.correostracker.base

import androidx.lifecycle.ViewModel
import androidx.annotation.CallSuper
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import net.kelmer.correostracker.util.NetworkInteractor
import net.kelmer.correostracker.util.SchedulerProvider
import javax.inject.Inject

/**
 * Created by gabriel on 25/03/2018.
 */
abstract class BaseViewModel : ViewModel() {

    protected val disposables: CompositeDisposable = CompositeDisposable()

    @Inject
    lateinit var networkInteractor: NetworkInteractor
    @Inject
    lateinit var schedulerProvider: SchedulerProvider

    @CallSuper
    fun onDestroy() {
        clearSubscriptions()
    }

    private fun clearSubscriptions() {
        disposables.clear()
        //disposables.dispose()
    }

    fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        clearSubscriptions()
    }


    @CallSuper
    open fun cancel(){
        clearSubscriptions()
    }

}