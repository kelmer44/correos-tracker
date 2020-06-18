package net.kelmer.correostracker.di.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module()
@InstallIn(ActivityComponent::class)
abstract class ActivityModule<T: AppCompatActivity> {

    @Binds
    abstract fun bindsActivity(activity: T) : AppCompatActivity


}