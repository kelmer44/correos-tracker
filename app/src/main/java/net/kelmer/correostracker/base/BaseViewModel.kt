package net.kelmer.correostracker.base

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import net.kelmer.correostracker.base.usecase.UseCase

/**
 * Created by gabriel on 25/03/2018.
 */
abstract class BaseViewModel(vararg useCases: UseCase<*, *>) : ViewModel() {

    private val disposables: CompositeDisposable = CompositeDisposable()

    fun addUseCase(useCase: UseCase<*, *>) {
        useCaseList.add(useCase)
    }
    private val useCaseList: MutableList<UseCase<*, *>> = useCases.asList().toMutableList()

    @CallSuper
    fun onDestroy() {
        clearSubscriptions()
    }

    private fun clearSubscriptions() {
        disposables.clear()
    }

    fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        clearSubscriptions()
        useCaseList.forEach { it.dispose() }
    }
}
