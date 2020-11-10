package net.kelmer.correostracker.base.usecase.rx

import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import net.kelmer.correostracker.data.Resource
import net.kelmer.correostracker.util.ext.toResource

abstract class RxSingleUseCase<in P, R> : RxUseCase<P, R>() {

    internal abstract fun buildUseCase(params: P): Single<R>

    override fun execute(params: P, onNext: (Resource<R>) -> Unit) {
        dispose()
        buildUseCase(params)
                .toResource(schedulerProvider)
                .subscribeBy(onNext = onNext)
                .track()
    }

}