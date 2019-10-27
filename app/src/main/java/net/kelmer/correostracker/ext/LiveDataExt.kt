package net.kelmer.correostracker.ext

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations

fun <T> LiveData<T>.observe(owner: LifecycleOwner, observer: (T?) -> Unit) =
        observe(owner, Observer<T> { v -> observer.invoke(v) })


fun <T> LiveData<T>.reobserve(owner: LifecycleOwner, observer: (T?) -> Unit) {
    removeObservers(owner)
    observe(owner, observer)
}

fun <X, Y> LiveData<X>.map(transformer: (X) -> Y): LiveData<Y> =
        Transformations.map(this, { transformer(it) })
