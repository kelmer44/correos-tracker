package net.kelmer.correostracker.ui.debug

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import net.kelmer.correostracker.BuildConfig
import net.kelmer.correostracker.R
import net.kelmer.correostracker.fragment.BaseFragment
import net.kelmer.correostracker.databinding.FragmentDebugBinding
import net.kelmer.correostracker.ui.debug.views.DebugView
import javax.inject.Inject

@AndroidEntryPoint
class DebugFragment : BaseFragment<FragmentDebugBinding>(R.layout.fragment_debug) {

    val viewModel: DebugViewModel by viewModels()

    private val debugListener = object : DebugView.DebugViewListener {
    }

    override fun bind(view: View): FragmentDebugBinding = FragmentDebugBinding.bind(view)
    override fun setupToolbar(toolbar: Toolbar) {
    }

    override fun loadUp(binding: FragmentDebugBinding, savedInstanceState: Bundle?) {
        val drawerContext = ContextThemeWrapper(activity, R.style.Theme_Debug)
        val debugView = DebugView(drawerContext)
        debugView.listener = debugListener

        debugView.setupBuildSection(
            BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE.toString(),
            BuildConfig.GIT_SHA,
            BuildConfig.GIT_TIMESTAMP
        )
        debugView.initLoggingSection()

        binding.debugScroll.addView(debugView)
    }
}
