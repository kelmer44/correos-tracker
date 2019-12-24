package net.kelmer.correostracker.di.activity

import androidx.appcompat.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [CommonActivityModule::class])
abstract class ActivityModule<T: AppCompatActivity> {

    @Binds
    abstract fun bindsActivity(activity: T) : AppCompatActivity

}