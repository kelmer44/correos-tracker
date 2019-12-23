package net.kelmer.correostracker.di.fragment

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import dagger.Provides
import net.kelmer.correostracker.di.qualifiers.ForActivity
import net.kelmer.correostracker.di.scopes.PerFragment

@Module
abstract class CommonFragmentModule {

    @Binds
    @ForActivity
    abstract fun bindsContext(activity: AppCompatActivity) : Context

}