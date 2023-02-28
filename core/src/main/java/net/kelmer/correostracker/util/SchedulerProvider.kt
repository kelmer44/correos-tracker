package net.kelmer.correostracker.util

import io.reactivex.Scheduler

/**
 * Created by gabriel on 30/01/2018.
 */
interface SchedulerProvider {

    fun ui(): Scheduler

    fun computation(): Scheduler

    fun newThread(): Scheduler

    fun io(): Scheduler
}
