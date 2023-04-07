package net.kelmer.correostracker.list

import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDisposable
import net.kelmer.correostracker.util.SchedulerProvider
import javax.inject.Inject

class ParcelListLifecycleObserver @Inject constructor(
    fragment: Fragment,
    private val schedulerProvider: SchedulerProvider,
    private val presenter: ParcelListPresenter
) : DefaultLifecycleObserver {

    private val viewModel: ParcelListViewModel by fragment.viewModels()

    override fun onStart(owner: LifecycleOwner) {
        viewModel.stateOnceAndStream
            .observeOn(schedulerProvider.ui())
            .autoDisposable(owner.scope(Lifecycle.Event.ON_STOP))
            .subscribe(
                {
                    presenter.bindState(it)
                },
                {
                    throw it
                }
            )
    }
}
