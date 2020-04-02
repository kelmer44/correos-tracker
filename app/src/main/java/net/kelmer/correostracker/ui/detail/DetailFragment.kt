package net.kelmer.correostracker.ui.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.crashlytics.android.Crashlytics
import kotlinx.android.synthetic.main.activity_detail.toolbar
import kotlinx.android.synthetic.main.fragment_detail.detail_loading
import kotlinx.android.synthetic.main.fragment_detail.error_container
import kotlinx.android.synthetic.main.fragment_detail.error_text
import kotlinx.android.synthetic.main.fragment_detail.parcelStatusRecyclerView
import net.kelmer.correostracker.R
import net.kelmer.correostracker.base.fragment.BaseFragment
import net.kelmer.correostracker.data.Result
import net.kelmer.correostracker.data.model.dto.ParcelDetailDTO
import net.kelmer.correostracker.data.network.exception.CorreosException
import net.kelmer.correostracker.ext.isVisible
import net.kelmer.correostracker.ext.observe
import net.kelmer.correostracker.ui.detail.adapter.DetailTimelineAdapter
import net.kelmer.correostracker.util.NetworkInteractor
import net.kelmer.correostracker.util.peso
import net.kelmer.correostracker.util.textOrElse
import timber.log.Timber


class DetailFragment : BaseFragment<ParcelDetailViewModel>() {

    override val layoutId: Int = R.layout.fragment_detail
    override val viewModelClass: Class<ParcelDetailViewModel> = ParcelDetailViewModel::class.java


    private val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(activity)
    private val adapterRecyclerView: DetailTimelineAdapter = DetailTimelineAdapter()

    private val parcelCode by lazy { activity?.intent?.getStringExtra(DetailActivity.KEY_PARCELCODE) }

    override fun loadUp(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)

        parcelStatusRecyclerView.layoutManager = linearLayoutManager
        parcelStatusRecyclerView.adapter = adapterRecyclerView

        viewModel.getParcel(parcelCode ?: "NONE")
        viewModel.parcel.observe(this) {

            it?.let {
                detail_loading.isVisible = it.inProgress
            }

            when (it) {
                is Result.InProgress -> {
                    error_container.isVisible = false
                }
                is Result.Success -> {
                    loadParcelInformation(it.data)
                }
                is Result.Failure -> {
                    error_container.isVisible = true
                    Timber.e(it.e, it.errorMessage)
                    when (it.e) {
                        is CorreosException -> {
                            Crashlytics.log("Controlled Exception Error $parcelCode")
                            Crashlytics.logException(it.e)
                            error_text.text = it.e.message
                        }
                        is NetworkInteractor.NetworkUnavailableException -> {
                            Crashlytics.log("Controlled Exception Error $parcelCode")
                            Crashlytics.logException(it.e)
                            error_text.text = getString(R.string.error_no_network)
                        }
                        else -> {
                            Crashlytics.log("Unknown Error $parcelCode")
                            Crashlytics.logException(it.e)
                            error_text.text = getString(R.string.error_unrecognized)
                        }
                    }

                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.parcel_refresh -> {
                viewModel.getParcel(parcelCode ?: "NONE")
            }
            R.id.parcel_info -> {
                alertDialog?.show()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    var alertDialog: AlertDialog? = null

    private fun loadParcelInformation(parcel: ParcelDetailDTO) {
        activity?.toolbar?.title = parcel.name
        adapterRecyclerView.updateStatus(parcel.states)
        parcelStatusRecyclerView.scrollToPosition(adapterRecyclerView.itemCount - 1)

        var ctx = context
        if (ctx != null) {

            val inflater = this.layoutInflater
            val parent = inflater.inflate(R.layout.parcel_info, null)

            var dialog = AlertDialog.Builder(ctx)
                    .setTitle(parcel.name)
                    .setPositiveButton(getString(android.R.string.ok)) { p0, p1 -> p0.dismiss() }
                    .setView(parent)
                    .create()

            parent.findViewById<TextView>(R.id.disclaimer)?.isVisible = !parcel.fechaCalculada.isNullOrEmpty()
            parent.findViewById<LinearLayout>(R.id.fecha_estimada_container)?.isVisible = !parcel.fechaCalculada.isNullOrEmpty()

            val peso = parent.findViewById<TextView>(R.id.masinfo_peso)
            val dimenContainer = parent.findViewById<LinearLayout>(R.id.dimen_container)
            val height = parent.findViewById<TextView>(R.id.masinfo_height)
            val width = parent.findViewById<TextView>(R.id.masinfo_width)
            val depth = parent.findViewById<TextView>(R.id.masinfo_depth)
            val dimensiones = parent.findViewById<TextView>(R.id.masinfo_dimensiones)
            val codProducto = parent.findViewById<TextView>(R.id.masinfo_codproducto)
            val ref = parent.findViewById<TextView>(R.id.masinfo_ref)
            val fecha = parent.findViewById<TextView>(R.id.masinfo_fechaestimada)
            val orElse = "N/A"
            if (parcel.refCliente == "referencia") {
                parcel.refCliente = ""
            }




            peso?.peso(parcel.peso, orElse)
            if (parcel.containsDimensions()) {
                height?.textOrElse(parcel.alto, orElse)
                width?.textOrElse(parcel.ancho, orElse)
                depth?.textOrElse(parcel.largo, orElse)
                dimensiones.text = getString(R.string.dimensiones_placeholder, parcel.alto, parcel.ancho, parcel.largo)
                dimenContainer.visibility = View.VISIBLE
            } else {
                dimenContainer.visibility = View.GONE
            }
            codProducto?.textOrElse(parcel.codProducto, orElse)
            ref?.textOrElse(parcel.refCliente, orElse)
            fecha?.textOrElse(parcel.fechaCalculada, orElse)




            alertDialog = dialog
        }

    }

}
