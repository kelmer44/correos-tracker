package net.kelmer.correostracker.data

/**
 * Created by gabriel on 25/03/2018.
 */
/**
 * Created by gabriel on 04/02/2018.
 */
sealed class Result<T>(val inProgress: Boolean) {

    class InProgress<T> : Result<T>(true) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            return true
        }

        override fun hashCode(): Int = javaClass.hashCode()
    }

    data class Success<T>(var data: T) : Result<T>(false)
    data class Failure<T>(val errorMessage: String?, val e: Throwable) : Result<T>(false)
    data class NetworkUnavailable<T>(val errorMessage: String?, val e: Throwable) : Result<T>(false)


    companion object {
        fun <T> inProgress(): Result<T> = InProgress()
        fun <T> success(data: T): Result<T> = Success(data)

        fun <T> failure(errorMessage: String, e: Throwable): Result<T> = Failure(errorMessage, e)
        fun <T> networkUnavailable(errorMessage: String, e:Throwable): Result<T> = NetworkUnavailable(errorMessage, e)
    }
}