package net.kelmer.correostracker.service.worker

import androidx.work.Worker
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class WorkerKey(val value: KClass<out Worker>)


@Module
abstract class ParcelPollModule {

    @Binds
    @IntoMap
    @WorkerKey(ParcelPollWorker::class)
    internal abstract fun bindMyWorkerFactory(worker: ParcelPollWorker.Factory): Worker


}