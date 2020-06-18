package net.kelmer.correostracker.di.fragment

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
abstract class FragmentModule<T: Fragment> {

}