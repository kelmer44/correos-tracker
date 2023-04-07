package net.kelmer.correostracker.detail

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import net.kelmer.correostracker.dataApi.model.dto.ParcelDetailDTO
import net.kelmer.correostracker.dataApi.model.exception.CorreosException
import net.kelmer.correostracker.dataApi.model.remote.CorreosApiEvent
import net.kelmer.correostracker.detail.adapter.DetailTimelineItem
import net.kelmer.correostracker.detail.compose.DetailScreen
import net.kelmer.correostracker.details.databinding.FragmentDetailBinding
import net.kelmer.correostracker.details.R
import net.kelmer.correostracker.ui.theme.CorreosTheme
import net.kelmer.correostracker.util.NetworkInteractor
import net.kelmer.correostracker.util.copyToClipboard
import net.kelmer.correostracker.util.ext.isVisible
import net.kelmer.correostracker.util.ext.peso
import net.kelmer.correostracker.util.ext.textOrElse
import timber.log.Timber
import javax.inject.Inject

class DetailPresenter @Inject constructor(
    private val fragment: Fragment,
    private val viewModel: ParcelDetailViewModel,
    private val groupieAdapter: GroupAdapter<GroupieViewHolder>,
    private val detailTimelineItem: DetailTimelineItem.Factory
) {
    private val binding = FragmentDetailBinding.bind(fragment.requireView())
    private val args: DetailFragmentArgs by fragment.navArgs()
    private val parcelCode = args.parcelCode

    private val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(fragment.requireContext())

    private var alertDialog: AlertDialog? = null

    init {
        NavigationUI.setupWithNavController(binding.detailToolbar, findNavController(fragment))
        setupToolbar(binding.detailToolbar)
        binding.parcelStatusRecyclerView.itemAnimator = null
        binding.parcelStatusRecyclerView.layoutManager = linearLayoutManager
        binding.parcelStatusRecyclerView.adapter = groupieAdapter
        viewModel.refresh()
    }

    private fun setupToolbar(toolbar: Toolbar) {
        binding.detailToolbar.title = parcelCode
        binding.detailToolbar.subtitle = parcelCode
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

    fun bindState(state: ParcelDetailViewModel.State) {

        binding.composeView.apply {
            setContent {
//                va/l darkTheme = fragment.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                CorreosTheme(false) {
                    DetailScreen(state)
                }
            }
        }
        binding.detailLoading.isVisible = state.isLoading
        binding.errorContainer.isVisible = state.error != null
        binding.parcelStatusRecyclerView.isVisible = state.error == null && !state.isLoading

        if (state.parcelDetail != null) {
            loadParcelInformation(state.parcelDetail)
        }

        state.parcelDetail?.states?.let {
            loadEventList(it)
        }

        if (state.error != null) {
            binding.errorText.text = state.error.message
            Timber.e(state.error, "Error ${state.error.message}")
            when (state.error) {
                is CorreosException -> {
                    FirebaseCrashlytics.getInstance().log("Controlled Exception Error $parcelCode")
                    FirebaseCrashlytics.getInstance().recordException(state.error)
                    binding.errorText.text = state.error.message
                }
                is NetworkInteractor.NetworkUnavailableException -> {
                    FirebaseCrashlytics.getInstance()
                        .log("Controlled Network Unavailable Exception Error $parcelCode")
                    FirebaseCrashlytics.getInstance().recordException(state.error)
                    binding.errorText.text = fragment.requireContext().getString(R.string.error_no_network)
                }
                else -> {
                    FirebaseCrashlytics.getInstance().log("Unknown Error $parcelCode")
                    FirebaseCrashlytics.getInstance().recordException(state.error)
                    binding.errorText.text = fragment.requireContext().getString(R.string.error_unrecognized)
                }
            }
        }
    }

    private fun loadEventList(events: List<CorreosApiEvent>) {
        groupieAdapter.update(events.map { event -> detailTimelineItem.create(event) })
        binding.parcelStatusRecyclerView.scrollToPosition(events.size - 1)
    }

    private fun loadParcelInformation(parcel: ParcelDetailDTO) {
        binding.detailToolbar.title = parcel.name
        binding.detailToolbar.subtitle = parcel.code

        val inflater = fragment.layoutInflater
        val parent = inflater.inflate(R.layout.parcel_info, null)

        val dialog = AlertDialog.Builder(fragment.requireContext())
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
                fragment.requireContext()
                    .getString(R.string.dimensiones_placeholder, parcel.alto, parcel.ancho, parcel.largo)
            dimenContainer.visibility = View.VISIBLE
        } else {
            dimenContainer.visibility = View.GONE
        }
        codProducto?.textOrElse(parcel.codProducto, orElse)
        ref?.textOrElse(parcel.refCliente, orElse)
        fecha?.textOrElse(parcel.fechaCalculada, orElse)

        copy.setOnClickListener {
            fragment.requireContext().copyToClipboard(parcel.code)
        }
        alertDialog = dialog
    }
}
