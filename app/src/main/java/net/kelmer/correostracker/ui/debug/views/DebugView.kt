package net.kelmer.correostracker.ui.debug.views

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import android.text.format.DateFormat.format
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.widget.FrameLayout
import dagger.hilt.android.AndroidEntryPoint
import net.kelmer.correostracker.databinding.ViewDebugContentBinding
import net.kelmer.correostracker.di.debug.LumberYard
import net.kelmer.correostracker.ui.activity.MainActivity
import net.kelmer.correostracker.ui.debug.dialog.LogsDialog
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * Created by gabriel on 01/11/2017.
 */
@AndroidEntryPoint
class DebugView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    @Inject
    lateinit var lumberYard: LumberYard

    var listener: DebugViewListener? = null

    interface DebugViewListener

    val binding: ViewDebugContentBinding = ViewDebugContentBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        setupDeviceSection()
        initAnalytics()
    }

    fun initLoggingSection() {
        binding.debugShowlogs.setOnClickListener {
            LogsDialog(
                context,
                lumberYard = lumberYard
            ).show()
        }
    }

    private fun initAnalytics() {
    }

    fun restartApp() {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
        Runtime.getRuntime().exit(0)
    }

    @SuppressLint("SetTextI18n")
    fun setupBuildSection(name: String, code: String, sha: String, timestamp: Long) {
        binding.debugBuildName.text = name
        binding.debugBuildCode.text = code
        binding.debugBuildSha.text = sha
        binding.debugBuildDate.text = getDate(timestamp)
    }

    private fun getDate(time: Long): String {
        val cal: Calendar = Calendar.getInstance(Locale.ENGLISH)
        cal.timeInMillis = time * 1000
        return format("dd-MM-yyyy HH:mm:ss", cal).toString()
    }

    @SuppressLint("SetTextI18n")
    private fun setupDeviceSection() {
        val displayMetrics = context.resources.displayMetrics
        val densityBucket = getDensityString(displayMetrics)
        binding.debugDeviceMake.text = Build.MANUFACTURER.substring(
            0,
            Build.MANUFACTURER.length.coerceAtMost(20)
        )
        binding.debugDeviceModel.text = Build.MODEL.substring(0, Build.MODEL.length.coerceAtMost(20))
        binding.debugDeviceResolution.text =
            displayMetrics.heightPixels.toString() + "x" + displayMetrics.widthPixels
        binding.debugDeviceDensity.text =
            displayMetrics.densityDpi.toString() + "dpi (" + densityBucket + ")"
        binding.debugDeviceRelease.text = Build.VERSION.RELEASE
        binding.debugDeviceApi.text = Build.VERSION.SDK_INT.toString()
        binding.debugDeviceInches.text = getInches(context).toString()
//        binding.debugDeviceIstablet.text = context.resources.getBoolean(R.bool.isTablet).toString()

        binding.debugDevice1dp.text = "${resources.displayMetrics.density * 1} px"
    }

    /**
     * Inches of the device
     */
    private fun getInches(context: Context): Double {
        val dm = context.resources.displayMetrics
        val x = (dm.widthPixels / dm.xdpi).toDouble().pow(2.0)
        val y = (dm.heightPixels / dm.ydpi).toDouble().pow(2.0)
        var screenInches = sqrt(x + y)
        //        Log.d("debug", "Screen inches : " + screenInches);
        screenInches = (screenInches * 10).roundToInt().toDouble() / 10
        return screenInches
    }

    private fun getDensityString(displayMetrics: DisplayMetrics): String {
        return when (displayMetrics.densityDpi) {
            DisplayMetrics.DENSITY_LOW -> "ldpi"
            DisplayMetrics.DENSITY_MEDIUM -> "mdpi"
            DisplayMetrics.DENSITY_HIGH -> "hdpi"
            DisplayMetrics.DENSITY_XHIGH -> "xhdpi"
            DisplayMetrics.DENSITY_XXHIGH -> "xxhdpi"
            DisplayMetrics.DENSITY_XXXHIGH -> "xxxhdpi"
            DisplayMetrics.DENSITY_TV -> "tvdpi"
            else -> displayMetrics.densityDpi.toString()
        }
    }

    private fun scanForActivity(cont: Context?): Activity? {
        when (cont) {
            null -> return null
            is Activity -> return cont
            is ContextWrapper -> return scanForActivity(cont.baseContext)
            else -> return null
        }
    }
}
