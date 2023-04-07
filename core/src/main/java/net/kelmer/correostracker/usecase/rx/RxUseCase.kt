package net.kelmer.correostracker.usecase.rx

import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.Disposable
import net.kelmer.correostracker.usecase.UseCase
import net.kelmer.correostracker.dataApi.Resource
import net.kelmer.correostracker.util.NetworkInteractor
import net.kelmer.correostracker.util.SchedulerProvider
import javax.inject.Inject

abstract class RxUseCase <in P, R> : UseCase<P, R>() {
    @Inject
    lateinit var schedulerProvider: SchedulerProvider
    @Inject
    lateinit var networkInteractor: NetworkInteractor

    private var disposable: Disposable? = null

    abstract fun execute(params: P, onNext: ((Resource<R>) -> Unit))

    override fun invoke(parameters: P, result: MutableLiveData<Resource<R>>) {
        execute(
            parameters,
            onNext = {
                result.value = it
            }
        )
    }

    protected fun Disposable.track(): Disposable {
        disposable = this
        return this
    }

    override fun dispose() {
        disposable?.dispose()
        disposable = null
    }
}
