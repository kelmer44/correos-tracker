package net.kelmer.correostracker.base

import android.arch.lifecycle.ViewModel
import android.support.annotation.CallSuper
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by gabriel on 25/03/2018.
 */
abstract class RxViewModel : ViewModel() {

    protected val disposables: CompositeDisposable = CompositeDisposable()

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