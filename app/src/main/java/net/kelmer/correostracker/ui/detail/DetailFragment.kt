package net.kelmer.correostracker.ui.detail

import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.fragment_detail.*
import net.kelmer.correostracker.ApplicationComponent
import net.kelmer.correostracker.R
import net.kelmer.correostracker.base.BaseFragment
import net.kelmer.correostracker.data.Result
import net.kelmer.correostracker.data.model.dto.ParcelDetailDTO
import net.kelmer.correostracker.ext.isVisible
import net.kelmer.correostracker.ext.observe
import timber.log.Timber

class DetailFragment : BaseFragment<ParcelDetailViewModel>() {

    private val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(activity)
    private val adapterRecyclerView: DetailTimelineAdapter = DetailTimelineAdapter()

    override fun injectDependencies(graph: ApplicationComponent) {
        val component = graph.plus(ParcelDetailModule())
        component
                .injectTo(this)
        component
                .injectTo(viewModel)
    }

    override val layoutId: Int = R.layout.fragment_detail
    override val viewModelClass: Class<ParcelDetailViewModel> = ParcelDetailViewModel::class.java


    private val parcelCode by lazy { activity.intent.getStringExtra(DetailActivity.KEY_PARCELCODE) }

    override fun loadUp() {
        setHasOptionsMenu(true)

        parcelStatusRecyclerView.layoutManager = linearLayoutManager
        parcelStatusRecyclerView.adapter = adapterRecyclerView

        viewModel.getParcel(parcelCode)
        viewModel.parcel.observe(this, {

            it?.let {
                    detail_loading.isVisible = it.inProgress

            }
            when(it){
                is Result.Success ->{
                    loadParcelInformation(it.data)
                }
                is Result.Failure -> {
                    Toast.makeText(context, "ERROR " + it.errorMessage, Toast.LENGTH_LONG).show()
                    Timber.e(it.errorMessage)
                }
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.parcel_refresh) {
            viewModel.getParcel(parcelCode)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadParcelInformation(parcelInformation: ParcelDetailDTO) {
        activity.toolbar.title = parcelInformation.name
        adapterRecyclerView.updateStatus(parcelInformation.states)
        parcelStatusRecyclerView.smoothScrollToPosition(parcelInformation.states.size-1)
    }

}
