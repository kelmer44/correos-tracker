package net.kelmer.correostracker.ui.detail

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.crashlytics.FirebaseCrashlytics
import net.kelmer.correostracker.R
import net.kelmer.correostracker.data.Resource
import net.kelmer.correostracker.data.model.dto.ParcelDetailDTO
import net.kelmer.correostracker.data.network.exception.CorreosException
import net.kelmer.correostracker.data.resolve
import net.kelmer.correostracker.databinding.FragmentDetailBinding
import net.kelmer.correostracker.ui.detail.adapter.DetailTimelineAdapter
import net.kelmer.correostracker.util.NetworkInteractor
import net.kelmer.correostracker.util.copyToClipboard
import net.kelmer.correostracker.util.ext.isVisible
import net.kelmer.correostracker.util.ext.textOrElse
import net.kelmer.correostracker.util.peso
import timber.log.Timber
import javax.inject.Inject

class DetailPresenter @Inject constructor(
    private val fragment: Fragment,
) {
    private val viewModel: ParcelDetailViewModel by fragment.viewModels()
    private val binding = FragmentDetailBinding.bind(fragment.requireView())
    private val args: DetailFragmentArgs by fragment.navArgs()

    private val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(fragment.requireContext())
    private val timelineAdapter: DetailTimelineAdapter = DetailTimelineAdapter()

    private var alertDialog: AlertDialog? = null

    init {
        NavigationUI.setupWithNavController(binding.detailToolbar, findNavController(fragment))
        setupToolbar(binding.detailToolbar)
        binding.parcelStatusRecyclerView.layoutManager = linearLayoutManager
        binding.parcelStatusRecyclerView.adapter = timelineAdapter

    }

    private fun setupToolbar(toolbar: Toolbar) {
        toolbar.inflateMenu(R.menu.menu_detail)
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.parcel_refresh -> {
                    viewModel.refresh()
                }
                R.id.parcel_info -> {
                    alertDialog?.show()
                }
            }
            true
        }
    }

    fun bindState(resource: Resource<ParcelDetailViewModel.State>) {
        binding.detailLoading.isVisible = resource.inProgress()
        resource.resolve(
            onError = {
                binding.errorContainer.isVisible = true
                Timber.e(it)
                when (it) {
                    is CorreosException -> {
                        FirebaseCrashlytics.getInstance().log("Controlled Exception Error ${args.parcelCode}")
                        FirebaseCrashlytics.getInstance().recordException(it)
                        binding.errorText.text = it.message
                    }
                    is NetworkInteractor.NetworkUnavailableException -> {
                        FirebaseCrashlytics.getInstance()
                            .log("Controlled Network Unavailable Exception Error ${args.parcelCode}")
                        FirebaseCrashlytics.getInstance().recordException(it)
                        binding.errorText.text = fragment.requireContext().getString(R.string.error_no_network)
                    }
                    else -> {
                        FirebaseCrashlytics.getInstance().log("Unknown Error ${args.parcelCode}")
                        FirebaseCrashlytics.getInstance().recordException(it)
                        binding.errorText.text = fragment.requireContext().getString(R.string.error_unrecognized)
                    }
                }
            },
            onSuccess = {
                loadParcelInformation(it.parcel)
            }
        )
    }

    private fun loadParcelInformation(parcel: ParcelDetailDTO) {
        binding.detailToolbar.title = parcel.name
        binding.detailToolbar.subtitle = parcel.code
        timelineAdapter.updateStatus(parcel.states)
        binding.parcelStatusRecyclerView.scrollToPosition(timelineAdapter.itemCount - 1)

        val context = fragment.requireContext()

        val inflater = fragment.layoutInflater
        val parent = inflater.inflate(R.layout.parcel_info, null)

        val dialog = AlertDialog.Builder(context)
            .setTitle(parcel.name)
            .setPositiveButton(fragment.getString(android.R.string.ok)) { p0, _ -> p0.dismiss() }
            .setView(parent)
            .create()

        parent.findViewById<TextView>(R.id.disclaimer)?.isVisible = !parcel.fechaCalculada.isNullOrEmpty()
        parent.findViewById<LinearLayout>(R.id.fecha_estimada_container)?.isVisible =
            !parcel.fechaCalculada.isNullOrEmpty()

        val peso = parent.findViewById<TextView>(R.id.masinfo_peso)
        val dimenContainer = parent.findViewById<LinearLayout>(R.id.dimen_container)
        val height = parent.findViewById<TextView>(R.id.masinfo_height)
        val width = parent.findViewById<TextView>(R.id.masinfo_width)
        val depth = parent.findViewById<TextView>(R.id.masinfo_depth)
        val dimensiones = parent.findViewById<TextView>(R.id.masinfo_dimensiones)
        val codProducto = parent.findViewById<TextView>(R.id.masinfo_codproducto)
        val ref = parent.findViewById<TextView>(R.id.masinfo_ref)
        val fecha = parent.findViewById<TextView>(R.id.masinfo_fechaestimada)
        val code = parent.findViewById<TextView>(R.id.masinfo_code)
        val copy = parent.findViewById<ImageView>(R.id.masinfo_copy)

        val orElse = "N/A"
        if (parcel.refCliente == "referencia") {
            parcel.refCliente = ""
        }

        code.text = parcel.code

        peso?.peso(parcel.peso, orElse)
        if (parcel.containsDimensions()) {
            height?.textOrElse(parcel.alto, orElse)
            width?.textOrElse(parcel.ancho, orElse)
            depth?.textOrElse(parcel.largo, orElse)
            dimensiones.text =
                context.getString(R.string.dimensiones_placeholder, parcel.alto, parcel.ancho, parcel.largo)
            dimenContainer.visibility = View.VISIBLE
        } else {
            dimenContainer.visibility = View.GONE
        }
        codProducto?.textOrElse(parcel.codProducto, orElse)
        ref?.textOrElse(parcel.refCliente, orElse)
        fecha?.textOrElse(parcel.fechaCalculada, orElse)

        copy.setOnClickListener {
            context.copyToClipboard(parcel.code)
        }
        alertDialog = dialog
    }
}
