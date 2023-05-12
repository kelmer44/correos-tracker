package net.kelmer.correostracker.iap

import androidx.fragment.app.FragmentActivity
import io.reactivex.Completable
import io.reactivex.Flowable

interface IapApi {
    /**
     * Launches purchase flow to buy premium
     */
    fun launchFlow(activity: FragmentActivity) : Completable

    /**
     * Stream that emits true or false at least, then only emits again if switched to true
     */
    fun isPremium() : Flowable<Boolean>
}
