package net.kelmer.correostracker.ui.list

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_parcel_list.*
import net.kelmer.correostracker.ApplicationComponent
import net.kelmer.correostracker.R
import net.kelmer.correostracker.base.BaseFragment
import net.kelmer.correostracker.data.Result
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.ext.observe
import net.kelmer.correostracker.ui.detail.DetailActivity
import timber.log.Timber
import android.R.attr.label
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE




/**
 * Created by gabriel on 25/03/2018.
 */
class ParcelListFragment : BaseFragment<ParcelListViewModel>() {

    override fun injectDependencies(graph: ApplicationComponent) {
        graph.injectTo(this)
        graph.injectTo(viewModel)
    }

    override val layoutId: Int = R.layout.fragment_parcel_list
    override val viewModelClass: Class<ParcelListViewModel> = ParcelListViewModel::class.java


    private val clickListener = object: ParcelClickListener{
        override fun longPress(parcelReference: LocalParcelReference) {
            val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("ParcelCode", parcelReference.code)
            clipboard.primaryClip = clip
            Toast.makeText(context, getString(R.string.clipboard_copied), Toast.LENGTH_LONG).show()
        }

        override fun click(parcelReference: LocalParcelReference) {
            var ctx = context
            ctx?.let {

                startActivity(DetailActivity.newIntent(ctx, parcelReference.code))
            }
        }

        override fun dots(view: View, parcelReference: LocalParcelReference) {
//            viewModel.deleteParcel(parcelReference
// )
            var ctx = context

            ctx?.let {
                val popup = PopupMenu(ctx, view)
                val inflater = popup.menuInflater
                inflater.inflate(R.menu.parcel_menu, popup.menu)
                popup.setOnMenuItemClickListener { item ->
                    //do your things in each of the following cases
                    when (item.itemId) {
                        R.id.menu_delete -> {
                            viewModel.deleteParcel(parcelReference)
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

    override fun loadUp() {
        setupRecyclerView()
        viewModel.retrieveParcelList()
        viewModel.parcelList.observe(this, {
            it?.let { i ->
                swipe_refresh.isRefreshing = i.inProgress
            }

            when (it) {
                is Result.Success -> {
                    adapter.updateItems(it.data)
                }
                is Result.Failure -> {
                    Toast.makeText(context, "ERROR!!!", Toast.LENGTH_LONG).show()
                }
            }
        })

        viewModel.deleteLiveData.observe(this, {
            when (it){
                is Result.Success -> {

                    Timber.w("Deleted ${it.data} elements!!")
                    adapter.notifyDataSetChanged()
                    rv_parcel_list.invalidate()
                }
                is Result.Failure -> {
                    Toast.makeText(context, "ERROR DELETING!", Toast.LENGTH_LONG).show()
                }
            }
        })


        viewModel.statusReports.observe(this, {
            it?.let {
                adapter.setLoading(it.codEnvio, false)
            }
        })
    }

    private fun setupRecyclerView() {
        val llm = LinearLayoutManager(context)
        rv_parcel_list.layoutManager = llm
        rv_parcel_list.adapter = adapter

        swipe_refresh.setOnRefreshListener{
            viewModel.retrieveParcelList()
        }
    }


    override fun onResume() {
        super.onResume()
        viewModel.retrieveParcelList()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)

        super.onCreateOptionsMenu(menu, inflater);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_refresh_all -> {
                viewModel.refresh(adapter.items)
                adapter.items.forEachIndexed { i, p ->
                    adapter.setLoading(p.code, true)
                }
            }
        }
        return true
    }
}