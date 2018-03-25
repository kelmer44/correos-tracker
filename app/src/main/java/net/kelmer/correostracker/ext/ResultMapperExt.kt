package net.kelmer.correostracker.ext

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import net.kelmer.correostracker.data.Result
import net.kelmer.correostracker.util.NetworkInteractor
import net.kelmer.correostracker.util.SchedulerProvider

/**
 * Created by gabriel on 08/02/2018.
 */

fun <T> Flowable<T>.toResult(schedulerProvider: SchedulerProvider): Flowable<Result<T>> {
    return compose { item ->
        item
                .map { Result.success(it) }
                .onErrorReturn { e -> Result.failure(e.message ?: "unknown", e) }
                .observeOn(schedulerProvider.ui())
                .startWith(Result.inProgress())
    }
}

fun <T> Observable<T>.toResult(networkRequest: Disposable, schedulerProvider: SchedulerProvider): Observable<Result<T>> {
    return compose { item ->
        item
                .map { Result.success(it) }
                .onErrorReturn { e ->
                    when (e) {
                        is NetworkInteractor.NetworkUnavailableException -> Result.networkUnavailable("NetworkUnavailable", e)
                        else ->
                            Result
                            .failure(e.message?:"Fail", e)
//                                e -> Result.failure(e.message ?: "unknown", e)
                    }

                }
                .doOnSubscribe {
                    networkRequest.dispose()
                }
//                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .startWith(Result.inProgress())

    }
}

fun <T> Single<T>.toResult(networkRequest: Disposable, schedulerProvider: SchedulerProvider): Observable<Result<T>> {
    return toObservable().toResult(networkRequest, schedulerProvider)
}

fun <T> Completable.toResult(networkRequest: Disposable, schedulerProvider: SchedulerProvider): Observable<Result<T>> {
    return toObservable<T>().toResult(networkRequest, schedulerProvider)
}
