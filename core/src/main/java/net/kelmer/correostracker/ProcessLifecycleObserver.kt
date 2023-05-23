package net.kelmer.correostracker

import javax.inject.Qualifier

/**
 * Annotate a [androidx.lifecycle.LifecycleObserver] with this qualifier to let it be registered in the
 * [androidx.lifecycle.ProcessLifecycleOwner].
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ProcessLifecycleObserver

/**
 * Annotate a [androidx.lifecycle.LifecycleObserver] with this qualifier to let it be registered in the
 * Activity Lifecycle.
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityLifecycleObserver
