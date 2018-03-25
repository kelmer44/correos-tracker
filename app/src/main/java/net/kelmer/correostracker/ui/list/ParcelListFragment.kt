package net.kelmer.correostracker.ui.list

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_parcel_list.*
import net.kelmer.correostracker.ApplicationComponent
import net.kelmer.correostracker.R
import net.kelmer.correostracker.base.BaseFragment
import net.kelmer.correostracker.data.Result
import net.kelmer.correostracker.ext.observe
import net.kelmer.correostracker.ui.detail.DetailActivity


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

    private val adapter = ParcelListAdapter({ p ->
        startActivity(DetailActivity.newIntent(context, p.code))
    })

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
                Toast.makeText(context, "Refresh all!!!", Toast.LENGTH_LONG).show()
            }
        }
        return true
    }
}