package net.kelmer.correostracker.util

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

/**
 * Created by Gabriel Sanmartín on 10/11/2020.
 */
class TrampolineSchedulerProvider : SchedulerProvider {
    override fun io(): Scheduler = Schedulers.trampoline()

    override fun ui(): Scheduler = Schedulers.trampoline()

    override fun computation(): Scheduler = Schedulers.trampoline()

    override fun newThread(): Scheduler = Schedulers.trampoline()
}
