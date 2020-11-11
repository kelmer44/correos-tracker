package net.kelmer.correostracker.di.fragment

import androidx.fragment.app.Fragment
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
abstract class FragmentModule<T : Fragment>
