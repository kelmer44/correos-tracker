package net.kelmer.correostracker.util

import android.net.ConnectivityManager
import io.reactivex.Completable
import net.kelmer.correostracker.util.NetworkInteractor
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by gabriel on 14/07/2017.
 */

@Singleton
class NetworkInteractorImpl @Inject constructor(
        private val connectivityManager: ConnectivityManager
) : NetworkInteractor {

    override fun hasNetworkConnection(): Boolean  =
        connectivityManager.activeNetworkInfo?.isConnectedOrConnecting ?: false


    override fun hasNetworkConnectionCompletable(): Completable =
            if (hasNetworkConnection()) {
                Completable.complete()
            } else {
                Completable.error { NetworkInteractor.NetworkUnavailableException() }
            }

}