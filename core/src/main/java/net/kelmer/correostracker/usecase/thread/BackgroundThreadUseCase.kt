package net.kelmer.correostracker.usecase.thread

import androidx.lifecycle.MutableLiveData
import net.kelmer.correostracker.usecase.UseCase
import net.kelmer.correostracker.usecase.scheduler.DefaultScheduler
import net.kelmer.correostracker.usecase.scheduler.Scheduler
import net.kelmer.correostracker.dataApi.Resource
import timber.log.Timber

abstract class BackgroundThreadUseCase<in P, R> : UseCase<P, R>() {

    private var taskScheduler: Scheduler = DefaultScheduler

    /** Executes the use case synchronously  */
    fun executeNow(parameters: P): Resource<R> {
        return try {
            Resource.Success(execute(parameters))
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

    /**
     * Override this to set the code to be executed.
     */
    @Throws(RuntimeException::class)
    protected abstract fun execute(parameters: P): R

    override fun invoke(parameters: P, result: MutableLiveData<Resource<R>>) {
        // result.value = Result.Loading TODO: add data to Loading to avoid glitches
        try {
            taskScheduler.execute {
                try {
                    execute(parameters).let { useCaseResult ->
                        result.postValue(Resource.Success(useCaseResult))
                    }
                } catch (e: Exception) {
                    Timber.e(e)
                    result.postValue(Resource.Failure(e))
                }
            }
        } catch (e: Exception) {
            Timber.d(e)
            result.postValue(Resource.Failure(e))
        }
    }
}
