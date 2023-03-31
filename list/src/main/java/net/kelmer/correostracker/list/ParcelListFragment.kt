package net.kelmer.correostracker.list

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint
import net.kelmer.correostracker.fragment.BaseFragment
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.data.resolve
import net.kelmer.correostracker.iap.InAppReviewService
import net.kelmer.correostracker.list.R
import net.kelmer.correostracker.list.adapter.ParcelClickListener
import net.kelmer.correostracker.list.adapter.ParcelListAdapter
import net.kelmer.correostracker.list.databinding.FragmentParcelListBinding
import net.kelmer.correostracker.list.featuredialog.featureBlurbDialog
import net.kelmer.correostracker.ui.themedialog.themeSelectionDialog
import net.kelmer.correostracker.util.copyToClipboard
import net.kelmer.correostracker.util.ext.isVisible
import net.kelmer.correostracker.widget.ConfirmDialog
import timber.log.Timber
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
