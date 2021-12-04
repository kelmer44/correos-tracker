package net.kelmer.correostracker.service.worker

import androidx.work.ListenableWorker
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import net.kelmer.correostracker.di.worker.ChildWorkerFactory
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class WorkerKey(val value: KClass<out ListenableWorker>)

@Module
@InstallIn(SingletonComponent::class)
abstract class ParcelPollModule {

    @Binds
    @IntoMap
    @WorkerKey(ParcelPollWorker::class)
    internal abstract fun bindMyWorkerFactory(worker: ParcelPollWorker.Factory): ChildWorkerFactory
}
