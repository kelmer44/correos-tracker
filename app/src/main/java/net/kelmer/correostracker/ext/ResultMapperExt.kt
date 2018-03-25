package net.kelmer.correostracker.ext

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
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

fun <T> Single<T>.toResult(schedulerProvider: SchedulerProvider): Observable<Result<T>> {
    return toObservable().toResult(schedulerProvider)
}


fun <T> Observable<T>.toResult(schedulerProvider: SchedulerProvider): Observable<Result<T>> {
    return compose { item ->
        item
                .map { Result.success(it) }

                .onErrorReturn { e ->
                    if (e is NetworkInteractor.NetworkUnavailableException)
                        Result.networkUnavailable(e)
                    else
                        Result.failure(e.message ?: "unknown", e)
                }
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .startWith(Result.inProgress())
    }
}


fun <T> Completable.toResult(schedulerProvider: SchedulerProvider): Observable<Result<T>> {
    return toObservable<T>().toResult(schedulerProvider)
}



fun <T> Single<T>.withNetwork(networkInteractor: NetworkInteractor)  = apply { networkInteractor.hasNetworkConnectionCompletable().andThen(this) }