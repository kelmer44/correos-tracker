package net.kelmer.correostracker.base.usecase.rx

import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import net.kelmer.correostracker.data.Resource
import net.kelmer.correostracker.ext.toResult

abstract class RxSingleUseCase<in P, R> : RxUseCase<P, R>() {

    internal abstract fun buildUseCase(params: P): Single<R>
    override fun execute(params: P, onNext: (Resource<R>) -> Unit) {
        dispose()
        buildUseCase(params)
                .toResult(schedulerProvider)
                .subscribeBy(onNext = onNext)
                .track()
    }

}