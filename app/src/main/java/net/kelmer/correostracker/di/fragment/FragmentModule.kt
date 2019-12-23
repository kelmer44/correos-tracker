package net.kelmer.correostracker.di.fragment

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes= [CommonFragmentModule::class])
abstract class FragmentModule<T: Fragment> {

    @Binds
    abstract fun bindsFragment(fragment: T) : Fragment

    @Module
    companion object {
        @JvmStatic
        @Provides
        fun providesActivity(fragment: Fragment): AppCompatActivity = fragment.requireActivity() as AppCompatActivity
    }
}