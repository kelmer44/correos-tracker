package net.kelmer.correostracker.ui.detail

import kotlinx.android.synthetic.main.fragment_detail.*
import net.kelmer.correostracker.ApplicationComponent
import net.kelmer.correostracker.R
import net.kelmer.correostracker.base.BaseFragment

class DetailFragment : BaseFragment<ParcelDetailViewModel>() {
    override fun injectDependencies(graph: ApplicationComponent) {
        graph.injectTo(this)
        graph.injectTo(viewModel)
    }

    override val layoutId: Int = R.layout.fragment_detail
    override val viewModelClass: Class<ParcelDetailViewModel> = ParcelDetailViewModel::class.java


    override fun loadUp() {
        var parcelCode = activity.intent.getStringExtra(DetailActivity.KEY_PARCELCODE)
        parcel_code.text = parcelCode

    }

}
