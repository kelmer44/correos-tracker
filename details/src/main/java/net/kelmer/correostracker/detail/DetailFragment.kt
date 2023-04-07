package net.kelmer.correostracker.detail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.bamtechmedia.dominguez.core.utils.stringArgument
import dagger.hilt.android.AndroidEntryPoint
import net.kelmer.correostracker.detail.adapter.DetailTimelineItem
import net.kelmer.correostracker.details.R
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {

    val parcelCode by stringArgument("parcel_code")

    @Inject
    lateinit var lifecycleObserverProvider : Provider<DetailLifecycleObserver>

    @Inject
    lateinit var detailTimelineItem: DetailTimelineItem.Factory

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycle.addObserver(lifecycleObserverProvider.get())
    }

    companion object {
        const val KEY_PARCELCODE = "parcel_code"

        fun newInstance(code: String): DetailFragment {
            return DetailFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_PARCELCODE, code)
                }
            }
        }
    }
}
