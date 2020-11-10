package net.kelmer.correostracker.util.ext

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import net.kelmer.correostracker.data.Resource
import net.kelmer.correostracker.util.NetworkInteractor
import net.kelmer.correostracker.util.SchedulerProvider

/**
 * Created by gabriel on 08/02/2018.
 */

fun <T> Flowable<T>.toResource(schedulerProvider: SchedulerProvider): Flowable<Resource<T>> {
    return compose { item ->
        item
                .map { Resource.success(it) }
                .onErrorReturn { e -> Resource.failure(e, e.message ?: "unknown" ) }
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .startWith(Resource.inProgress())
    }
}

fun <T> Single<T>.toResource(schedulerProvider: SchedulerProvider): Observable<Resource<T>> {
    return toObservable().toResource(schedulerProvider)
}


fun <T> Observable<T>.toResource(schedulerProvider: SchedulerProvider): Observable<Resource<T>> {
    return compose { item ->
        item
                .map { Resource.success(it) }
                .onErrorReturn { e ->
                    Resource.failure(e)
                }
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .startWith(Resource.inProgress())
    }
}


fun <T> Completable.toResource(schedulerProvider: SchedulerProvider): Observable<Resource<T>> {
    return toObservable<T>().toResource(schedulerProvider)
}


fun <T> Flowable<T>.withNetwork(networkInteractor: NetworkInteractor) = apply { networkInteractor.hasNetworkConnectionCompletable().andThen(this) }
fun <T> Single<T>.withNetwork(networkInteractor: NetworkInteractor) = apply { networkInteractor.hasNetworkConnectionCompletable().andThen(this) }