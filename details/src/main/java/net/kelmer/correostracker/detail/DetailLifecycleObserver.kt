package net.kelmer.correostracker.detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDisposable
import net.kelmer.correostracker.util.SchedulerProvider
import timber.log.Timber
import javax.inject.Inject

class DetailLifecycleObserver @Inject constructor(
    private val detailPresenter: DetailPresenter,
    private val schedulerProvider: SchedulerProvider,
    private val viewModel: ParcelDetailViewModel
) : DefaultLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {
        viewModel.stateOnceAndStream
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .autoDisposable(owner.scope(Lifecycle.Event.ON_STOP))
            .subscribe(
                { detailPresenter.bindState(it)},
                {}
            )
    }
}
