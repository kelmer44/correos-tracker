package net.kelmer.correostracker.list

import android.content.Intent
import android.net.Uri
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
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import net.kelmer.correostracker.dataApi.model.local.LocalParcelReference
import net.kelmer.correostracker.iap.InAppReviewService
import net.kelmer.correostracker.list.adapter.ParcelClickListener
import net.kelmer.correostracker.list.adapter.ParcelListItem
import net.kelmer.correostracker.list.compose.ParcelList
import net.kelmer.correostracker.list.compose.Parcels
import net.kelmer.correostracker.list.databinding.FragmentParcelListBinding
import net.kelmer.correostracker.list.featuredialog.featureBlurbDialog
import net.kelmer.correostracker.ui.themedialog.themeSelectionDialog
import net.kelmer.correostracker.util.copyToClipboard
import net.kelmer.correostracker.util.ext.isVisible
import net.kelmer.correostracker.widget.ConfirmDialog
import javax.inject.Inject

class ParcelListPresenter @Inject constructor(
    private val parcelListItemFactory: ParcelListItem.Factory,
    private val fragment: Fragment,
    private val adapter: GroupAdapter<GroupieViewHolder>,
    private val inAppReviewService: InAppReviewService
) {
    private val binding = FragmentParcelListBinding.bind(fragment.requireView())
    private val viewModel: ParcelListViewModel by fragment.viewModels()


    init {




        inAppReviewService.showIfNeeded()
        if (!viewModel.showFeature()) {
            showFeature()
            viewModel.setShownFeature()
        }

        NavigationUI.setupWithNavController(binding.listToolbar, fragment.findNavController())
        binding.rvParcelList.adapter = adapter
        binding.rvParcelList.itemAnimator = null
        binding.rvParcelList.layoutManager = LinearLayoutManager(binding.rvParcelList.context)

        binding.swipeRefresh.setOnRefreshListener {
            refreshFromRemote()
        }

        setupToolbar(binding.listToolbar)

        binding.fab.setOnClickListener {
            val request = NavDeepLinkRequest.Builder
                .fromUri("correostracker://create".toUri())
                .build()
            fragment.findNavController()
                .navigate(
                    request, NavOptions.Builder()
                        .setEnterAnim(R.anim.nav_default_enter_anim)
                        .setPopEnterAnim(R.anim.nav_default_pop_enter_anim)
                        .build()
                )
        }
    }

    fun bindState(state: ParcelListViewModel.State) {
//
//        binding.composeView.apply {
//            setContent {
//                Parcels(state = state)
//            }
//        }

        binding.swipeRefresh.isRefreshing = state.loading
        binding.emptyState.isVisible = state.list.isEmpty()

        val itemList = state.list.map { parcelListItemFactory.create(it, clickListener) }
        adapter.updateAsync(itemList)

        if (state.error != null) {
            FirebaseCrashlytics.getInstance().recordException(state.error)
            Toast.makeText(fragment.requireContext(), "ERROR!!!", Toast.LENGTH_LONG).show()
        }
    }

    var searchView: SearchView? = null

    private fun setupToolbar(toolbar: Toolbar) {

        toolbar.inflateMenu(R.menu.menu_list)
        val searchViewItem = toolbar.menu.findItem(R.id.app_search)
        searchView = MenuItemCompat.getActionView(searchViewItem) as SearchView
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchView?.clearFocus()
                viewModel.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.filter(newText)
                return false
            }
        })

        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.app_refresh_all -> {
                    refreshFromRemote()
                }
                R.id.app_theme -> {
                    themeSelectionDialog(fragment.requireContext()) {
                        viewModel.setTheme(it.code)
                    }.show()
                }
                R.id.app_about -> {
                    showFeature()
                }
            }
            true
        }
    }

    private fun showFeature() {
        featureBlurbDialog(
            context = fragment.requireContext(),
            titleText = R.string.feature_dialog_title,
            okText = android.R.string.ok,
            okListener = {
            },
            githubListener = {
                val url = "https://github.com/kelmer44/correos-tracker"
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                fragment.startActivity(i)
            },
        ).show()
    }

    private fun refreshFromRemote() {
        viewModel.refresh()
    }

    private val clickListener = object : ParcelClickListener {
        override fun longPress(parcelReference: LocalParcelReference) {
            fragment.context?.copyToClipboard(parcelReference.trackingCode)
        }

        override fun click(parcelReference: LocalParcelReference) {
            val request = NavDeepLinkRequest.Builder
                .fromUri("correostracker://details/${parcelReference.trackingCode}".toUri())
                .build()
            fragment.findNavController().navigate(
                request, NavOptions.Builder()
                    .setEnterAnim(R.anim.nav_default_enter_anim)
                    .setPopEnterAnim(R.anim.nav_default_pop_enter_anim)
                    .build()
            )
        }

        override fun dots(view: View, parcelReference: LocalParcelReference) {
            val popup = PopupMenu(fragment.requireContext(), view)
            val inflater = popup.menuInflater
            inflater.inflate(R.menu.parcel_menu, popup.menu)
            popup.menu.findItem(R.id.menu_enable_notifications).isVisible = !parcelReference.notify
            popup.menu.findItem(R.id.menu_disable_notifications).isVisible = parcelReference.notify
            popup.setOnMenuItemClickListener { item ->
                // do your things in each of the following cases
                when (item.itemId) {
                    R.id.menu_delete -> {
                        ConfirmDialog.confirmDialog(
                            fragment.requireContext(),
                            R.string.delete_confirm_title,
                            R.string.delete_confirm_desc
                        ) {
                            viewModel.deleteParcel(parcelReference)
                        }
                        true
                    }
                    R.id.menu_enable_notifications -> {
                        viewModel.enableNotifications(parcelReference.trackingCode)
                        true
                    }
                    R.id.menu_disable_notifications -> {
                        viewModel.disableNotifications(parcelReference.trackingCode)
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }

        override fun update(parcelReference: LocalParcelReference) {}
    }
}
