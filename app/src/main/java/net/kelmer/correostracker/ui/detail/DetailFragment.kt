package net.kelmer.correostracker.ui.detail

import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.fragment_detail.*
import net.kelmer.correostracker.ApplicationComponent
import net.kelmer.correostracker.R
import net.kelmer.correostracker.base.BaseFragment
import net.kelmer.correostracker.data.Result
import net.kelmer.correostracker.data.model.dto.ParcelDetailDTO
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


    override fun loadUp() {
        var parcelCode = activity.intent.getStringExtra(DetailActivity.KEY_PARCELCODE)

        parcelStatusRecyclerView.layoutManager = linearLayoutManager
        parcelStatusRecyclerView.adapter = adapterRecyclerView

        viewModel.getParcel(parcelCode)
        viewModel.parcel.observe(this, {
            when(it){
                is Result.Success ->{
                    loadParcelInformation(it.data)
                }
                is Result.Failure -> {
                    Timber.e(it.errorMessage)
                }
            }
        })

    }

    private fun loadParcelInformation(parcelInformation: ParcelDetailDTO) {
        activity.toolbar.title = parcelInformation.name
        adapterRecyclerView.updateStatus(parcelInformation.states)
        parcelStatusRecyclerView.smoothScrollToPosition(parcelInformation.states.size-1)
    }

}
