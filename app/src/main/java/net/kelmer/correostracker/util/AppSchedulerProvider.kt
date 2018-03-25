package net.kelmer.correostracker.util

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by gabriel on 30/01/2018.
 */
class AppSchedulerProvider : SchedulerProvider {
    override fun ui(): Scheduler =
        AndroidSchedulers.mainThread()


    override fun computation(): Scheduler = Schedulers.computation()

    override fun newThread(): Scheduler = Schedulers.newThread()

    override fun io(): Scheduler = Schedulers.io()
}