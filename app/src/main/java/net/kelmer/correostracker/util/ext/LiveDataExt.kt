package net.kelmer.correostracker.util.ext

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import net.kelmer.correostracker.data.Resource

fun <T> LiveData<T>.subscribe(owner: LifecycleOwner, observer: (T) -> Unit) =
        observe(owner, Observer<T> { v ->
            v?.let {
                observer.invoke(v)
            }
        })

fun <T> LiveData<Resource<T>>.observeResource(
        owner: LifecycleOwner,
        onError: (Throwable) -> Unit = {},
        onSuccess: (T) -> Unit
) =
        observe(owner, Observer<Resource<T>> { v ->
            v?.let {
                if (it is Resource.Success) {
                    onSuccess(it.data)
                } else if (it is Resource.Failure) {
                    onError(it.exception)
                }
            }
        })




fun <X, Y> LiveData<X>.map(transformer: (X) -> Y): LiveData<Y> =
        Transformations.map(this) { transformer(it) }
