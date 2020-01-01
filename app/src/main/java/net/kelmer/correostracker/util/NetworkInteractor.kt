package net.kelmer.correostracker.util

import io.reactivex.*

/**
 * Manages controlling whether network connection is available or not.
 *
 * Created by gabriel on 14/07/2017.
 */
interface NetworkInteractor {

    fun hasNetworkConnection(): Boolean

    fun hasNetworkConnectionCompletable(): Completable



    fun <T> flowable() : FlowableTransformer<T, T> {
        return  FlowableTransformer{observable->
            this.hasNetworkConnectionCompletable().andThen(observable)
        }
    }

    fun <T> observable() : ObservableTransformer<T, T> {
        return  ObservableTransformer{observable->
            this.hasNetworkConnectionCompletable().andThen(observable)
        }
    }

    fun <T> single() : SingleTransformer<T, T> {
        return  SingleTransformer{observable->
            this.hasNetworkConnectionCompletable().andThen(observable)
        }
    }
    fun completable() : CompletableTransformer {
        return CompletableTransformer { completable->
            this.hasNetworkConnectionCompletable().andThen(completable)
        }
    }

    
    class NetworkUnavailableException: Throwable("No network available!")
}