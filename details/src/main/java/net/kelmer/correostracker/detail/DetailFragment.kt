package net.kelmer.correostracker.detail

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.bamtechmedia.dominguez.core.utils.stringArgument
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import net.kelmer.correostracker.details.R
import net.kelmer.correostracker.data.model.dto.ParcelDetailDTO
import net.kelmer.correostracker.data.model.exception.CorreosException
import net.kelmer.correostracker.data.resolve
import net.kelmer.correostracker.detail.adapter.DetailTimelineItem
import net.kelmer.correostracker.details.databinding.FragmentDetailBinding
import net.kelmer.correostracker.fragment.BaseFragment
import net.kelmer.correostracker.util.NetworkInteractor
import net.kelmer.correostracker.util.copyToClipboard
import net.kelmer.correostracker.util.ext.isVisible
import net.kelmer.correostracker.util.ext.peso
import net.kelmer.correostracker.util.ext.textOrElse
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>(R.layout.fragment_detail) {

    @Inject
    lateinit var detailTimelineItem: DetailTimelineItem.Factory

    @Inject
    lateinit var groupieAdapter: GroupAdapter<GroupieViewHolder>

    private val viewModel: ParcelDetailViewModel by viewModels()

    private val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(activity)

    private val parcelCode by stringArgument("parcel_code")

    override fun loadUp(binding: FragmentDetailBinding, savedInstanceState: Bundle?) {
        NavigationUI.setupWithNavController(binding.detailToolbar, findNavController())
        setupToolbar(binding.detailToolbar)

        Timber.v("Loading details for parcel $parcelCode")

        binding.parcelStatusRecyclerView.layoutManager = linearLayoutManager
        binding.parcelStatusRecyclerView.adapter = groupieAdapter

        viewModel.statusResult.observe(this) { resource ->
            binding.detailLoading.isVisible = resource.inProgress()
            resource.resolve(
                onError = {
                    binding.errorContainer.isVisible = true
                    Timber.e(it)
                    when (it) {
                        is CorreosException -> {
                            FirebaseCrashlytics.getInstance().log("Controlled Exception Error $parcelCode")
                            FirebaseCrashlytics.getInstance().recordException(it)
                            binding.errorText.text = it.message
                        }
                        is NetworkInteractor.NetworkUnavailableException -> {
                            FirebaseCrashlytics.getInstance().log("Controlled Network Unavailable Exception Error $parcelCode")
                            FirebaseCrashlytics.getInstance().recordException(it)
                            binding.errorText.text = getString(R.string.error_no_network)
                        }
                        else -> {
                            FirebaseCrashlytics.getInstance().log("Unknown Error $parcelCode")
                            FirebaseCrashlytics.getInstance().recordException(it)
                            binding.errorText.text = getString(R.string.error_unrecognized)
                        }
                    }
                },
                onSuccess = {
                    loadParcelInformation(it)
                }
            )
        }
    }

    override fun setupToolbar(toolbar: Toolbar) {
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

    var alertDialog: AlertDialog? = null

    private fun loadParcelInformation(parcel: ParcelDetailDTO) {
        binding?.let {
            it.detailToolbar.title = parcel.name
            it.detailToolbar.subtitle = parcel.code

            groupieAdapter.update(parcel.states.map { event -> detailTimelineItem.create(event) })

            it.parcelStatusRecyclerView.scrollToPosition(parcel.states.size - 1)

            val ctx = context
            if (ctx != null) {

                val inflater = this.layoutInflater
                val parent = inflater.inflate(R.layout.parcel_info, null)

                val dialog = AlertDialog.Builder(ctx)
                    .setTitle(parcel.name)
                    .setPositiveButton(getString(android.R.string.ok)) { p0, _ -> p0.dismiss() }
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
                    dimensiones.text = getString(R.string.dimensiones_placeholder, parcel.alto, parcel.ancho, parcel.largo)
                    dimenContainer.visibility = View.VISIBLE
                } else {
                    dimenContainer.visibility = View.GONE
                }
                codProducto?.textOrElse(parcel.codProducto, orElse)
                ref?.textOrElse(parcel.refCliente, orElse)
                fecha?.textOrElse(parcel.fechaCalculada, orElse)

                copy.setOnClickListener {
                    context?.copyToClipboard(parcel.code)
                }
                alertDialog = dialog
            }
        }
    }

    companion object {
        const val KEY_PARCELCODE = "parcel_code"

        fun newInstance(code: String): DetailFragment {
            return DetailFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_PARCELCODE, code)
                }
            }
        }
    }

    override fun bind(view: View): FragmentDetailBinding = FragmentDetailBinding.bind(view)
}
