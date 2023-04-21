package net.kelmer.correostracker.create

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.zxing.integration.android.IntentIntegrator
import dagger.hilt.android.AndroidEntryPoint
import net.kelmer.correostracker.create.databinding.FragmentCreateParcelBinding
import net.kelmer.correostracker.fragment.BaseFragment
import net.kelmer.correostracker.dataApi.Resource
import net.kelmer.correostracker.dataApi.model.local.LocalParcelReference
import net.kelmer.correostracker.dataApi.model.exception.WrongCodeException
import net.kelmer.correostracker.dataApi.resolve
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by gabriel on 25/03/2018.
 */
@AndroidEntryPoint
class CreateParcelFragment : Fragment(R.layout.fragment_create_parcel) {

    @Inject
    lateinit var lifecycleObserverProvider : Provider<CreateParcelLifecycleObserver>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycle.addObserver(lifecycleObserverProvider.get())
    }
}
