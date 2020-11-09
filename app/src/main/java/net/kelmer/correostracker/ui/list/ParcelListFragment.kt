package net.kelmer.correostracker.ui.list

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.android.synthetic.main.fragment_parcel_list.empty_state
import kotlinx.android.synthetic.main.fragment_parcel_list.rv_parcel_list
import kotlinx.android.synthetic.main.fragment_parcel_list.swipe_refresh
import net.kelmer.correostracker.R
import net.kelmer.correostracker.base.fragment.BaseFragment
import net.kelmer.correostracker.customviews.ConfirmDialog
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.data.resolve
import net.kelmer.correostracker.ext.isVisible
import net.kelmer.correostracker.ui.featuredialog.featureBlurbDialog
import net.kelmer.correostracker.ui.list.adapter.ParcelClickListener
import net.kelmer.correostracker.ui.list.adapter.ParcelListAdapter
import net.kelmer.correostracker.ui.themedialog.themeSelectionDialog
import net.kelmer.correostracker.util.copyToClipboard
import timber.log.Timber
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_list.fab
import kotlinx.android.synthetic.main.fragment_parcel_list.list_toolbar
import net.kelmer.correostracker.data.Resource


/**
 * Created by gabriel on 25/03/2018.
 */
@AndroidEntryPoint
class ParcelListFragment : BaseFragment(R.layout.fragment_parcel_list) {

    private val viewModel: ParcelListViewModel by viewModels()

    private val clickListener = object : ParcelClickListener {
        override fun longPress(parcelReference: LocalParcelReference) {
            context?.copyToClipboard(parcelReference.trackingCode)
        }

        override fun click(parcelReference: LocalParcelReference) {
            val action = ParcelListFragmentDirections.actionParcelListFragmentToDetailFragment(parcelReference.trackingCode)
            findNavController().navigate(action)
        }

        override fun dots(view: View, parcelReference: LocalParcelReference) {
            val ctx = context

            ctx?.let {
                val popup = PopupMenu(ctx, view)
                val inflater = popup.menuInflater
                inflater.inflate(R.menu.parcel_menu, popup.menu)
                popup.menu.findItem(R.id.menu_enable_notifications).isVisible = !parcelReference.notify
                popup.menu.findItem(R.id.menu_disable_notifications).isVisible = parcelReference.notify
                popup.setOnMenuItemClickListener { item ->
                    //do your things in each of the following cases
                    when (item.itemId) {
                        R.id.menu_delete -> {
                            ConfirmDialog.confirmDialog(requireContext(),
                                    R.string.delete_confirm_title,
                                    R.string.delete_confirm_desc) {
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

        }

        override fun update(parcelReference: LocalParcelReference) {
        }

    }

    private val adapter = ParcelListAdapter(clickListener)

    override fun loadUp(savedInstanceState: Bundle?) {

        NavigationUI.setupWithNavController(list_toolbar, findNavController())

        setupToolbar()
        setupRecyclerView()

        fab.setOnClickListener {
            findNavController().navigate(ParcelListFragmentDirections.actionParcelListFragmentToCreateParcelFragment())
        }

        if (!viewModel.showFeature()) {
            showFeature()
            viewModel.setShownFeature()
        }

        viewModel.parcelList.observe(viewLifecycleOwner) { resource ->
            swipe_refresh.isRefreshing = resource.inProgress()
            resource.resolve(
                    onSuccess = {
                        empty_state.isVisible = it.isEmpty()
                        adapter.updateItems(it)
                        adapter.filter(searchView?.query?.toString() ?: "")
                    },
                    onError = {
                        FirebaseCrashlytics.getInstance().recordException(it)
                        Toast.makeText(context, "ERROR!!!", Toast.LENGTH_LONG).show()
                    }
            )
        }

        viewModel.deleteResult.observe(viewLifecycleOwner) { resource ->
            resource.resolve(
                    onError = {
                        Toast.makeText(context, "ERROR DELETING!", Toast.LENGTH_LONG).show()
                    },
                    onSuccess = {
                        Timber.w("Deleted $it elements!!")
                        adapter.notifyDataSetChanged()
                        rv_parcel_list.invalidate()
                    }
            )
        }

        viewModel.statusReports.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    val codigo = resource.data?.codEnvio
                    if (codigo != null) {
                        Timber.i("Status report for $codigo")
                        adapter.setLoading(codigo, false)
                    }
                }
                is Resource.Failure -> {
                    Timber.e(resource.exception, resource.message)
                }
                else -> {

                }
            }
        }

    }

    override fun setupToolbar() {

        list_toolbar.inflateMenu(R.menu.menu_list)
        val searchViewItem = list_toolbar.menu.findItem(R.id.app_search)
        searchView = MenuItemCompat.getActionView(searchViewItem) as SearchView
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchView?.clearFocus()
                adapter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter(newText)
                return false
            }
        })

        list_toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.app_refresh_all -> {
                    viewModel.refresh(adapter.getAllItems())
                    adapter.getAllItems().forEachIndexed { i, p ->
                        adapter.setLoading(p.trackingCode, true)
                    }
                }
                R.id.app_theme -> {
                    themeSelectionDialog(requireContext()) {
                        //                    Timber.i("Theme selected: $it")
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

    private fun setupRecyclerView() {
        val llm = LinearLayoutManager(context)
        rv_parcel_list.layoutManager = llm
        rv_parcel_list.adapter = adapter

        swipe_refresh.setOnRefreshListener {
            refreshFromRemote()
        }
    }

    private fun refreshFromRemote() {
        viewModel.refresh(adapter.getAllItems())
        adapter.getAllItems().forEach { p ->
            adapter.setLoading(p.trackingCode, true)
        }
        if (adapter.getAllItems().isEmpty()) {
            swipe_refresh.isRefreshing = false
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.retrieveParcelList()
    }

    var searchView: SearchView? = null

    private fun showFeature() {
        featureBlurbDialog(
                context = requireContext(),
                titleText = R.string.feature_dialog_title,
                okText = android.R.string.ok,
                okListener = {
                },
                githubListener = {
                    val url = "https://github.com/kelmer44/correos-tracker"
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(url)
                    startActivity(i)
                },
        ).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        refreshFromRemote()
    }

}