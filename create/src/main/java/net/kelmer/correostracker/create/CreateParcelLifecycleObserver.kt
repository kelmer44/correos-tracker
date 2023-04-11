package net.kelmer.correostracker.create

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDisposable
import net.kelmer.correostracker.util.SchedulerProvider
import javax.inject.Inject

class CreateParcelLifecycleObserver @Inject constructor(
    private val detailPresenter: CreatePresenter,
    private val schedulerProvider: SchedulerProvider,
    private val viewModel: CreateParcelViewModel
) : DefaultLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {
        viewModel.stateOnceAndStream
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .autoDisposable(owner.scope(Lifecycle.Event.ON_STOP))
            .subscribe(
                { detailPresenter.bindState(it) },
                {}
            )
    }
}
