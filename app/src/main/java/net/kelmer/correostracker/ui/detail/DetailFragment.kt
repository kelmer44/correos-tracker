package net.kelmer.correostracker.ui.detail

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint
import net.kelmer.correostracker.R
import net.kelmer.correostracker.base.fragment.BaseFragment
import net.kelmer.correostracker.data.model.dto.ParcelDetailDTO
import net.kelmer.correostracker.data.network.exception.CorreosException
import net.kelmer.correostracker.data.resolve
import net.kelmer.correostracker.databinding.FragmentDetailBinding
import net.kelmer.correostracker.ui.detail.adapter.DetailTimelineAdapter
import net.kelmer.correostracker.util.NetworkInteractor
import net.kelmer.correostracker.util.copyToClipboard
import net.kelmer.correostracker.util.ext.isVisible
import net.kelmer.correostracker.util.ext.textOrElse
import net.kelmer.correostracker.util.peso
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {

    @Inject
    lateinit var detailLifecycleObserver: Provider<DetailLifecycleObserver>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycle.addObserver(detailLifecycleObserver.get())
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
