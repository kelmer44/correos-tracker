package net.kelmer.correostracker.iap.billingrx.lifecycle

import io.reactivex.Flowable

interface Connectable<T> {

    fun connect() : Flowable<T>
}
