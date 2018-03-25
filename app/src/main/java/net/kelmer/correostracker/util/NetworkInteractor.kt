package net.kelmer.correostracker.util

import io.reactivex.Completable

/**
 * Manages controlling whether network connection is available or not.
 *
 * Created by gabriel on 14/07/2017.
 */
interface NetworkInteractor {

    fun hasNetworkConnection(): Boolean

    fun hasNetworkConnectionCompletable(): Completable

    class NetworkUnavailableException: Throwable("No network available!")
}