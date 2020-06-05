package net.kelmer.correostracker.data

import net.kelmer.correostracker.util.NetworkInteractor
import timber.log.Timber


/**
 * Created by gabriel on 04/02/2018.
 */
sealed class Resource<out R> {

    data class Success<out T>(val data: T) : Resource<T>()
    data class Failure(val exception: Throwable, val message: String? = exception.message) : Resource<Nothing>(){
        fun isNetworkUnavailable() = exception is NetworkInteractor.NetworkUnavailableException
        fun log() = Timber.e(exception, message)
    }
    object InProgress : Resource<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Failure -> "Error[exception=$exception]"
            InProgress -> "Loading"
        }
    }

    fun inProgress() = this is InProgress

    companion object {
        fun <T> success(data: T): Resource<T> = Success(data)
        fun <T> failure(e: Throwable) : Resource<T> = Failure(exception = e)
        fun <T> failure(e: Throwable, message: String) : Resource<T> = Failure(exception = e, message = message)
        fun <T> inProgress(): Resource<T> = InProgress
    }
}

/**
 * `true` if [Resource] is of type [Success] & holds non-null [Success.data].
 */
val Resource<*>.succeeded
    get() = this is Resource.Success && data != null

fun <T> Resource<T>.successOr(fallback: T): T {
    return (this as? Resource.Success<T>)?.data ?: fallback
}

