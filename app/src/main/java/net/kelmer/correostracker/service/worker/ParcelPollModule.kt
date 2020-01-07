package net.kelmer.correostracker.service.worker

import androidx.work.ListenableWorker
import androidx.work.Worker
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import net.kelmer.correostracker.di.worker.ChildWorkerFactory
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class WorkerKey(val value: KClass<out ListenableWorker>)


@Module
abstract class ParcelPollModule {

    @Binds
    @IntoMap
    @WorkerKey(ParcelPollWorker::class)
    internal abstract fun bindMyWorkerFactory(worker: ParcelPollWorker.Factory): ChildWorkerFactory


}
