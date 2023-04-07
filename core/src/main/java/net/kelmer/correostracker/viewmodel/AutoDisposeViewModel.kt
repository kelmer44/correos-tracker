package net.kelmer.correostracker.viewmodel

import androidx.lifecycle.ViewModel
import com.uber.autodispose.ScopeProvider
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.flowables.ConnectableFlowable
import io.reactivex.subjects.CompletableSubject

abstract class AutoDisposeViewModel : ViewModel() {

    private val scopeSubject = CompletableSubject.create()

    /**
     * Will be disposed when [ViewModel.onCleared] is called.
     */
    private val viewModelDisposable = CompositeDisposable()

    /**
     * Scope until [onCleared] is called.
     */
    val viewModelScope: ScopeProvider = ScopeProvider { scopeSubject }

    protected fun <T : Any> ConnectableFlowable<T>.connectInViewModelScope(): Flowable<T> {
        return autoConnect(1) { viewModelDisposable.add(it) }
    }

    override fun onCleared() {
        scopeSubject.onComplete()
        viewModelDisposable.dispose()
        super.onCleared()
    }
}
