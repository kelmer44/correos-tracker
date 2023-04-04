package net.kelmer.correostracker.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by gabriel on 25/03/2018.
 */
@AndroidEntryPoint
class ParcelListFragment : Fragment(R.layout.fragment_parcel_list) {

    @Inject
    lateinit var lifecycleObserverProvider : Provider<ParcelListLifecycleObserver>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycle.addObserver(lifecycleObserverProvider.get())
    }
}
