package net.kelmer.correostracker.di

import net.kelmer.correostracker.BuildConfig
import net.kelmer.correostracker.BuildInfo
import javax.inject.Inject

class BuildInfoImpl @Inject constructor() : BuildInfo {
    override val versionName: String = BuildConfig.VERSION_NAME
    override val isDebug: Boolean = BuildConfig.DEBUG
    override val versionCode: Int = BuildConfig.VERSION_CODE
}
