package net.kelmer.correostracker.util

import android.content.Context
import android.content.pm.ApplicationInfo
import androidx.viewbinding.BuildConfig
import net.kelmer.correostracker.BuildInfo

object Util {

    fun isTestDevice(context: Context) : Boolean =
     0 != context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE ||
    android.provider.Settings.System.getString(context.contentResolver, "firebase.test.lab") == "true"
}