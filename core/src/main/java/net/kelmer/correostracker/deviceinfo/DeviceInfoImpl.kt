package net.kelmer.correostracker.deviceinfo

import android.content.Context
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DeviceInfoImpl @Inject constructor(
    @ApplicationContext context: Context
): DeviceInfo {
    override val model: String = Build.MODEL.substring(0, Build.MODEL.length.coerceAtMost(20))
    override val manufacturer: String = Build.MANUFACTURER.substring(
        0,
        Build.MANUFACTURER.length.coerceAtMost(20)
    )
    override val deviceWidthPixels: Int = context.resources.displayMetrics.widthPixels
    override val deviceHeightPixels: Int = context.resources.displayMetrics.heightPixels
    override val density: Int = context.resources.displayMetrics.densityDpi
    override val density1dp: Float = context.resources.displayMetrics.density * 1

}
