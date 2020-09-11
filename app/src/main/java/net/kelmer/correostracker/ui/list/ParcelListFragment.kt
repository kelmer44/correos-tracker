package net.kelmer.correostracker.ui.list

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.crashlytics.android.Crashlytics
import kotlinx.android.synthetic.main.fragment_parcel_list.empty_state
import kotlinx.android.synthetic.main.fragment_parcel_list.rv_parcel_list
import kotlinx.android.synthetic.main.fragment_parcel_list.swipe_refresh
import net.kelmer.correostracker.R
import net.kelmer.correostracker.base.fragment.BaseFragment
import net.kelmer.correostracker.customviews.ConfirmDialog
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.data.resolve
import net.kelmer.correostracker.ext.isVisible
import net.kelmer.correostracker.ext.observe
import net.kelmer.correostracker.ui.detail.DetailActivity
import net.kelmer.correostracker.ui.featuredialog.featureBlurbDialog
import net.kelmer.correostracker.ui.list.adapter.ParcelClickListener
import net.kelmer.correostracker.ui.list.adapter.ParcelListAdapter
import net.kelmer.correostracker.ui.themedialog.themeSelectionDialog
import timber.log.Timber


/**
 * Created by gabriel on 25/03/2018.
 */
class ParcelListFragment : BaseFragment<ParcelListViewModel>() {


    override val layoutId: Int = R.layout.fragment_parcel_list
    override val viewModelClass: Class<ParcelListViewModel> = ParcelListViewModel::class.java


    private val clickListener = object : ParcelClickListener {
        override fun longPress(parcelReference: LocalParcelReference) {
            val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("ParcelCode", parcelReference.trackingCode)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, getString(R.string.clipboard_copied), Toast.LENGTH_LONG).show()
        }

        override fun click(parcelReference: LocalParcelReference) {
            var ctx = context
            ctx?.let {
                startActivity(DetailActivity.newIntent(ctx, parcelReference.trackingCode))
            }
        }

        override fun dots(view: View, parcelReference: LocalParcelReference) {
            var ctx = context

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun loadUp(savedInstanceState: Bundle?) {
        setupRecyclerView()

        if (!viewModel.showFeature()) {
            showFeature()
            viewModel.setShownFeature()
        }

        viewModel.retrieveParcelList()
        viewModel.getParcelList().observe(this) { resource ->
            swipe_refresh.isRefreshing = resource.inProgress()
            resource.resolve(
                    onSuccess = {
                        empty_state.isVisible = it.isEmpty()
                        adapter.updateItems(it)
                        adapter.filter(searchView?.query?.toString() ?: "")
                    },
                    onError = {
                        Crashlytics.logException(it)
                        Toast.makeText(context, "ERROR!!!", Toast.LENGTH_LONG).show()
                    }
            )
        }

        viewModel.getDeleteResult().observe(this) { resource ->
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


        viewModel.statusReports.observe(this) { parcel ->
            parcel?.codEnvio?.let { codigo ->
                adapter.setLoading(codigo, false)
            }
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
    }

    override fun onResume() {
        super.onResume()
        viewModel.retrieveParcelList()
    }

    var searchView: SearchView? = null
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)

        val searchViewItem = menu.findItem(R.id.app_search)
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
        super.onCreateOptionsMenu(menu, inflater);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_refresh_all -> {
                viewModel.refresh(adapter.getAllItems())
                adapter.getAllItems().forEachIndexed { i, p ->
                    adapter.setLoading(p.trackingCode, true)
                }
            }
            R.id.app_theme -> {
                themeSelectionDialog(requireContext()) {
                    Timber.i("Theme selected: $it")
                }.show()
            }
            R.id.app_about -> {
                showFeature()
            }
        }
        return true
    }

    private fun showFeature() {
        featureBlurbDialog(requireContext(),
                R.string.feature_dialog_title,
                android.R.string.ok,
                {
                },
                {
                    val url = "https://ko-fi.com/kelmer"
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(url)
                    startActivity(i)
                }).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        refreshFromRemote()
    }

}