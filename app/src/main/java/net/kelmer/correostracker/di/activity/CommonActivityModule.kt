package net.kelmer.correostracker.di.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import net.kelmer.correostracker.di.qualifiers.ForActivity

@Module
abstract class CommonActivityModule {

    @Binds
    @ForActivity
    abstract fun bindsContext(activity: AppCompatActivity): Context

}