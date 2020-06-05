package net.kelmer.correostracker.base.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import net.kelmer.correostracker.data.Resource

abstract class UseCase<in P, R> {

    /** Executes the use case asynchronously and places the [Resource] in a MutableLiveData
     *
     * @param parameters the input parameters to run the use case with
     * @param result the MutableLiveData where the result is posted to
     *
     */
    abstract operator fun invoke(parameters: P, result: MutableLiveData<Resource<R>>)

    /** Executes the use case asynchronously and returns a [Resource] in a new LiveData object.
     *
     * @return an observable [LiveData] with a [Resource].
     *
     * @param parameters the input parameters to run the use case with
     */
    operator fun invoke(parameters: P): LiveData<Resource<R>> {
        val liveCallback: MutableLiveData<Resource<R>> = MutableLiveData()
        this(parameters, liveCallback)
        return liveCallback
    }

}


operator fun <R> UseCase<Unit, R>.invoke(): LiveData<Resource<R>> = this(Unit)
operator fun <R> UseCase<Unit, R>.invoke(result: MutableLiveData<Resource<R>>) = this(kotlin.Unit, result)